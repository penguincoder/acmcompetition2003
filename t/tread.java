import java.io.*;
import java.util.regex.*;
import java.util.*;
import java.lang.Math;
import java.text.NumberFormat;

public class tread	{
	final static String end = "10";
	
	public static void chirp ( int tokencount, StringTokenizer token, PrintWriter out )	{
		int time = 0, incline = 0;
		String one = token.nextToken();
		String two = token.nextToken();
		for ( int c = 0; c < 3; c++ )	{
			if ( (one.equals ( "0" ) || one.equals ( "1" )) && (two.equals ( "8" ) || two.equals ( "9" )) )	{
				time += Math.pow ( 2, c );
			}
			one = token.nextToken();
			two = token.nextToken();
		}
		for ( int c = 0; c < 3; c++ )	{
			if ( (one.equals ( "0" ) || one.equals ( "1" )) && (two.equals ( "8" ) || two.equals ( "9" )) )	{
				incline += Math.pow ( 2, c );
			}
			one = token.nextToken();
			two = token.nextToken();
		}
		try	{
			NumberFormat f = NumberFormat.getIntegerInstance();
			f.setMinimumIntegerDigits ( 2 );
			int minutes = (int)Math.floor ( tokencount / 180 );
			int seconds = (int)Math.floor ( (tokencount - 1) % 180 / 3 ) - 1;
			out.println ( f.format ( minutes ) + ":" + f.format ( seconds ) + " Speed " + time + " Inclination " + incline );
		}
		catch ( Exception e )	{e.printStackTrace();}
	}
	
	public static void main ( String[] args )	{
		BufferedReader input;
		PrintWriter output;
		int programs = 0;
		
		try	{
			input = new BufferedReader ( new FileReader ( "tread.in") );
			output = new PrintWriter ( new FileOutputStream ( "tread.out" ) );
			
			programs = Integer.parseInt ( input.readLine() );
			for ( int count = 1; count <= programs; count++ )	{
				output.println ( "Program " + count );
				
				String line = input.readLine();
				while ( !line.endsWith ( end ) )	{
					line += " " + input.readLine();
				}
				StringTokenizer token = new StringTokenizer ( line );
				int tokencount = token.countTokens();
				String current = token.nextToken();
				
				while ( tokencount - 16 >= tokencount - token.countTokens() )	{
					if ( current.equals ( "4" ) || current.equals ( "5" ) )	{
						StringTokenizer mytoke = new StringTokenizer ( line );
						int ttemp = token.countTokens() + 1;
						for ( int f = mytoke.countTokens(); f <= ttemp; f-- )	{mytoke.nextToken();}
						String temp = mytoke.nextToken();
						if ( temp.equals ( "4" ) || temp.equals ( "5" ) )	{
							String token1 = "", token2 = "";
							token1 = mytoke.nextToken();
							if ( token1.equals ( "0" ) || token1.equals ( "1" ) )	{
								token2 = mytoke.nextToken();
								if ( token2.equals ( "8" ) || token2.equals ( "9" ) )	{
									chirp ( tokencount - mytoke.countTokens(), mytoke, output );
									while ( token.countTokens() != mytoke.countTokens() )	{token.nextToken();}
								}
							}
						}
					}
					if ( token.hasMoreTokens() )	{current = token.nextToken();}
					else	{break;}
				}
				
			}
			
			output.flush();
			output.close();
		}
		catch ( Exception e )	{e.printStackTrace();}
		
	}
	
}
