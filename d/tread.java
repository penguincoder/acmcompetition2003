import java.io.*;
import java.util.regex.*;
import java.util.StringTokenizer;
import java.lang.Math;
import java.text.NumberFormat;

public class tread	{
	
	public static void main ( String[] args )	{
		final String end = "10";
		final Pattern chirp = Pattern.compile ( "[45] [45] [01] [89]" );
		BufferedReader input;
		PrintWriter output;
		int programs = 0;
		
		try	{
			input = new BufferedReader ( new FileReader ( "tread.in" ) );
			output = new PrintWriter ( new FileOutputStream ( "tread.out" ) );
			
			programs = Integer.parseInt ( input.readLine() );
			for ( int count = 1; count <= programs; count++ )	{
				output.println ( "Program " + count );
				
				String line = input.readLine();
				while ( !line.endsWith ( end ) )	{line += " " + input.readLine();}
				
				int stringpos = 0;
				Matcher m = chirp.matcher ( line );
				
				while ( m.find ( stringpos ) )	{
					stringpos = m.start();
					int time = 0, incline = 0;
					String tmp = line.substring ( m.end() );
					if ( tmp.length() < 12 )
						break;
					StringTokenizer token = new StringTokenizer ( tmp );
					for ( int c = 0; c < 3; c++ )	{
						String t = token.nextToken();
						String u = token.nextToken();
						if ( (t.equals ( "0" ) || t.equals ( "1" )) && (u.equals ( "8" ) || u.equals ( "9" )) )	{
							time += Math.pow ( 2, c );
						}
					}
					for ( int c = 0; c < 3; c++ )	{
						String t = token.nextToken();
						String u = token.nextToken();
						if ( (t.equals ( "0" ) || t.equals ( "1" )) && (u.equals ( "8" ) || u.equals ( "9" )) )	{
							incline += Math.pow ( 2, c );
						}
					}
					
					NumberFormat f = NumberFormat.getIntegerInstance();
					f.setMinimumIntegerDigits ( 2 );
					int minutes = (int)Math.floor ( stringpos / 360 );
					int seconds = (int)Math.floor ( stringpos % 360 / 6 );
					output.println ( f.format ( minutes ) + ":" + f.format ( seconds ) + " Speed " + time + " Inclination " + incline );
					stringpos += 32;
				}
			}
			
			input.close(); output.flush(); output.close();
		}
		catch ( Exception e )	{e.printStackTrace();}
		
	}
	
}
