package simulator.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

/*
 * Examples of command-line parameters:
 * 
 *  -h
 *  -i resources/examples/ex4.4body.txt -s 100
 *  -i resources/examples/ex4.4body.txt -o resources/examples/ex4.4body.out -s 100
 *  -i resources/examples/ex4.4body.txt -o resources/examples/ex4.4body.out -s 100 -gl ftcg
 *  -i resources/examples/ex4.4body.txt -o resources/examples/ex4.4body.out -s 100 -gl nlug
 *
 */

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONObject;

import simulator.control.Controller;
import simulator.factories.BasicBodyBuilder;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.Factory;
import simulator.factories.FallingToCenterGravityBuilder;
import simulator.factories.MassLosingBodyBuilder;
import simulator.factories.NewtonUniversalGravitationBuilder;
import simulator.factories.NoGravityBuilder;
import simulator.model.Body;
import simulator.model.GravityLaws;
import simulator.model.PhysicsSimulator;
import simulator.view.MainWindow;

public class Main {

	// default values for some parameters
	//
	private final static Double _dtimeDefaultValue = 2500.0;

	// some attributes to stores values corresponding to command-line parameters
	//
	private static Double _dtime = null;
	private static String _inFile = null;
	private static String _outputFile = null;
	private static String _executionMode = "batch";
	private final static Integer _defaultValueSteps = 150;
	private static Integer _ValueSteps = null;
	private static JSONObject _gravityLawsInfo = null;

	// factories
	private static Factory<Body> _bodyFactory;
	private static Factory<GravityLaws> _gravityLawsFactory;
	
	public static Factory<Body> get_bodyFactory() {
		return _bodyFactory;
	}
	
	private static void init() {
		// initialize the bodies factory
		ArrayList<Builder<Body>> bodyBuilders = new ArrayList<>();
		bodyBuilders.add(new BasicBodyBuilder());
		bodyBuilders.add(new MassLosingBodyBuilder());
		_bodyFactory = new BuilderBasedFactory<Body>(bodyBuilders);

		// initialize the gravity laws factory
		ArrayList<Builder<GravityLaws>> gravityLawsBuilders = new ArrayList<>();
		gravityLawsBuilders.add(new NewtonUniversalGravitationBuilder());
		gravityLawsBuilders.add(new FallingToCenterGravityBuilder());
		gravityLawsBuilders.add(new NoGravityBuilder());
		_gravityLawsFactory = new BuilderBasedFactory<GravityLaws>(gravityLawsBuilders);
	}

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseHelpOption(line, cmdLineOptions);
			parseInFileOption(line);
			parseDeltaTimeOption(line);
			parseGravityLawsOption(line);
			parseOutputFileOption(line);
			parseStepsOption(line);
			
			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		// help
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message.").build());

		// input file
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Bodies JSON input file.").build());

		// delta-time
		cmdLineOptions.addOption(Option.builder("dt").longOpt("delta-time").hasArg()
				.desc("A double representing actual time, in seconds, per simulation step. Default value: "
						+ _dtimeDefaultValue + ".")
				.build());

		// gravity laws -- there is a workaround to make it work even when
		// _gravityLawsFactory is null. 
		//
		String gravityLawsValues = "N/A";
		String defaultGravityLawsValue = "N/A";
		if (_gravityLawsFactory != null) {
			gravityLawsValues = "";
			for (JSONObject fe : _gravityLawsFactory.getInfo()) {
				if (gravityLawsValues.length() > 0) {
					gravityLawsValues = gravityLawsValues + ", ";
				}
				gravityLawsValues = gravityLawsValues + "'" + fe.getString("type") + "' (" + fe.getString("desc") + ")";
			}
			defaultGravityLawsValue = _gravityLawsFactory.getInfo().get(0).getString("type");
		}
		cmdLineOptions.addOption(Option.builder("gl").longOpt("gravity-laws").hasArg()
				.desc("Gravity laws to be used in the simulator. Possible values: " + gravityLawsValues
						+ ". Default value: '" + defaultGravityLawsValue + "'.")
				.build());
		//Output
		cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg().desc(" Output file, where output is written. "
				+ "Default value: the standard output.").build());
		
		//Steps
		cmdLineOptions.addOption(Option.builder("s").longOpt("steps").hasArg().desc(" An integer representing the number of "
				+ "simulation steps. Default value: 150.").build());
		
		// execution mode
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc(" Execution Mode. Possible values: ’batch’ (Batch mode),\n" + 
				"’gui’ (Graphical User Interface mode). Default value: ’batch’.").build());
		
		return cmdLineOptions;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		if (_inFile == null) {
			throw new ParseException("An input file of bodies is required");
		}
		
	}

	private static void parseDeltaTimeOption(CommandLine line) throws ParseException {
		String dt = line.getOptionValue("dt", _dtimeDefaultValue.toString());
		try {
			_dtime = Double.parseDouble(dt);
			assert (_dtime > 0);
		} catch (Exception e) {
			throw new ParseException("Invalid delta-time value: " + dt);
		}
	}

	private static void parseGravityLawsOption(CommandLine line) throws ParseException {

		// this line is just a work around to make it work even when _gravityLawsFactory
		// is null, you can remove it when've defined _gravityLawsFactory
		if (_gravityLawsFactory == null)
			return;

		String gl = line.getOptionValue("gl");
		if (gl != null) {
			for (JSONObject fe : _gravityLawsFactory.getInfo()) {
				if (gl.equals(fe.getString("type"))) {
					_gravityLawsInfo = fe;
					break;
				}
			}
			if (_gravityLawsInfo == null) {
				throw new ParseException("Invalid gravity laws: " + gl);
			}
		} else {
			_gravityLawsInfo = _gravityLawsFactory.getInfo().get(0);
		}
	}
	
	private static void parseOutputFileOption(CommandLine line) throws ParseException {
		_outputFile = line.getOptionValue("o");
	}
	
	private static void parseStepsOption(CommandLine line) throws ParseException {
		String s = line.getOptionValue("s", _defaultValueSteps.toString());
		try {
			_ValueSteps = Integer.parseInt(s);
			assert (_ValueSteps > 0);
		} catch (Exception e) {
			throw new ParseException("Valor invalido para el limite de tiempo: " + s +  ".");
		}
	}
	
	private static void parseExecutionMode(String[] args) throws ParseException {
		Options cmdLineOptions = buildOptions();

		CommandLineParser parser = new DefaultParser();
		CommandLine line = parser.parse(cmdLineOptions, args);
		String executionMode = line.getOptionValue("m");
		
		if(executionMode != null) {
			if(executionMode.equalsIgnoreCase("batch") || executionMode.equalsIgnoreCase("gui"))
				_executionMode = executionMode;
			else throw new ParseException("Modo de ejecucion invalido.");
		}
				
	}
	

	private static void startBatchMode(String[] args) throws Exception {
		parseArgs(args);
		// create and connect components, then start the simulator
		InputStream is = new FileInputStream(new File(_inFile));
		OutputStream os = _outputFile == null ? System.out : new FileOutputStream(new File(_outputFile));
		GravityLaws gravityLaws = _gravityLawsFactory.createInstance(_gravityLawsInfo);
		PhysicsSimulator sim = new PhysicsSimulator(gravityLaws,_dtime);
		Controller ctrl = new Controller(sim, _bodyFactory, _gravityLawsFactory);
		try
		{
			ctrl.loadBodies(is);
			ctrl.run(_ValueSteps, os);
			System.out.println("Done!");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			if (e instanceof NumberFormatException) System.out.print("Se esperaba un numero en la conversion.");
			else System.out.print(e.getMessage());
			System.out.println(" Se ha terminado el programa generando resultados vacï¿½os.");
		}
	}
	
	private static void startGUImode(String[] args) throws Exception{
		Options cmdLineOptions = buildOptions();

		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			_inFile = line.getOptionValue("i");
			parseDeltaTimeOption(line);
			parseGravityLawsOption(line);
			
			GravityLaws gravityLaws = _gravityLawsFactory.createInstance(_gravityLawsInfo);
			PhysicsSimulator sim = new PhysicsSimulator(gravityLaws,_dtime);
			Controller ctrl = new Controller(sim, _bodyFactory, _gravityLawsFactory);
			
			
			
			SwingUtilities.invokeAndWait(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						new MainWindow(ctrl);
						if (_inFile != null) {
							InputStream is = new FileInputStream(new File(_inFile));
							ctrl.loadBodies(is);
						}
						
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}catch(Exception e) {
			throw new ParseException("Problema con el input");
		}
	}

	private static void start(String[] args) throws Exception {
		parseExecutionMode(args);
		if(_executionMode.equalsIgnoreCase("batch")) startBatchMode(args);
		else startGUImode(args);
	}

	public static void main(String[] args) {
		try {
			init();
			start(args);
		} catch (Exception e) {
			System.err.println("Something went wrong ...");
			System.err.println();
			e.printStackTrace();
		}
	}
}
