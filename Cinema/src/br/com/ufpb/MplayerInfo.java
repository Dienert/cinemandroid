package br.com.ufpb;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Properties;


public class MplayerInfo {
	String exec;
	PrintProcessOutput pp;
	ErrorHandler listener = null;

	public void setErrorHandler(ErrorHandler listener){
		this.listener=listener;
	}

	public PrintProcessOutput getPrintProcessOutput() {
		return pp;
	}

	public String getSaida() {
		return pp.getSaida().toString();
	}

	MplayerInfo(String exec) {
		this.exec = exec;
	}

	public void run() {
		try {

			long time = System.currentTimeMillis();
			
			pp = new PrintProcessOutput(exec, listener);
			pp.start();
			
			int errorCode = pp.getProcess().waitFor();
			
			//System.out.println(String.format("Took %d ms to run '%s'", System.currentTimeMillis() - time, exec));
			
			if(errorCode != 0) {
				if(listener != null) {
					listener.setErrorCode(errorCode);
					listener.fatalError(pp.getSaida().toString());
				} else {
					throw new Exception(String.format("Error to execute '%s', error code: %d", exec, errorCode));
				}
			}
			pp.getProcess().destroy();
			
		} catch (Throwable t) {
			if(listener != null) {	
				listener.fatalError(pp.getSaida().toString());
			} else {
				throw new RuntimeException(t);
			}
		}

	}
	
	private class PrintProcessOutput extends Thread {

		private StringBuffer saida;
		private Process process = null;
		private InputStream in = null;
		private PrintProcessOutput printError = null;
		private ErrorHandler listener = null;
		private boolean isError = false;

		public PrintProcessOutput(Process p) {
			process = p;

			if (p != null) {
				in = p.getInputStream();
			}

			printError = new PrintProcessOutput(p.getErrorStream(), false);
			process = p;
		}
		
		public PrintProcessOutput(Process p, ErrorHandler e) {
			process = p;

			if (p != null) {
				in = p.getInputStream();
			}
			
			if( e != null) {
				this.listener = e;
				printError = new PrintProcessOutput(p.getErrorStream(), true);
			} else {
				printError = new PrintProcessOutput(p.getErrorStream(), false);
			}
			
			process = p;
		}

		public StringBuffer getSaida() {
			return saida;
		}
		
		protected PrintProcessOutput(InputStream o, boolean error) {
			in = o;
			this.isError = error;
		}

		public Process getProcess() {
			return process;
		}

		public PrintProcessOutput(String command, ErrorHandler e) throws IOException {
			process = Runtime.getRuntime().exec(command);

			if (process != null) {
				in = process.getInputStream();
			}

			if( e != null) {
				this.listener = e;
				printError = new PrintProcessOutput(process.getErrorStream(), true);
			} else {
				printError = new PrintProcessOutput(process.getErrorStream(), false);
			}			
		}
		
		@Override
		public void run() {
			saida=new StringBuffer();

			if (printError != null) {
				printError.start();
			}

			String line = null;
			BufferedReader bin = new BufferedReader(new InputStreamReader(in));

			do {
				try {
					line = bin.readLine();
					if (line != null) {
						saida.append(line+"\n");
						
						if(this.isError) {
							if(listener!=null) {
								listener.error(line);
							}
						} else if(listener!=null) {
							listener.info(line);
						}
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			} while (line != null);
		}
	}
	
	
	public static int getLength(File file) {
		
		String path = file.getAbsolutePath();
		path.replace("/", File.separator);
		System.out.println("###PATH: "+path);
		MplayerInfo media = new MplayerInfo("mplayer -identify -really-quiet -vo null -ao null -frames 0 "+path);
		media.run();
		String saida = media.getSaida();
		
		Properties p = new Properties();
		StringReader r = new StringReader(saida);
		try {
			p.load(r);
			return Math.round(Float.parseFloat(p.getProperty("ID_LENGTH")))*1000;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
		
	}
	
	public static void main(String[] args) {
		File file = new File("/home/dienert/Desktop/Red5/"+"1.flv");
		if (file.exists()) {
			System.out.println(getLength(file));
		} else {
			System.out.println("Arquivo não existe");
		}
	}
	
}
