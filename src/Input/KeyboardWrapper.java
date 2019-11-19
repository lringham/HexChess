package Input;

import org.lwjgl.input.Keyboard;

public class KeyboardWrapper 
{

	public static String getPressed() 
	{
		String pressedText = "";
		
		while (Keyboard.next())
		{
			if (Keyboard.getEventKeyState())
			{
				int key = Keyboard.getEventKey();
				pressedText += convertKey(key);
			}
		}
		
		return pressedText;
	}

	public static String convertKey(int key)
	{	
		String keyString = "";
		if(!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		{
			switch (key) 
			{
			case Keyboard.KEY_0:
				keyString = "0";
				break;
			case Keyboard.KEY_1:
				keyString = "1";
				break;
			case Keyboard.KEY_2:
				keyString = "2";
				break;
			case Keyboard.KEY_3:
				keyString = "3";
				break;
			case Keyboard.KEY_4:
				keyString = "4";
				break;
			case Keyboard.KEY_5:
				keyString = "5";
				break;
			case Keyboard.KEY_6:
				keyString = "6";
				break;
			case Keyboard.KEY_7:
				keyString = "7";
				break;
			case Keyboard.KEY_8:
				keyString = "8";
				break;
			case Keyboard.KEY_9:
				keyString = "9";
				break;
			case Keyboard.KEY_NUMPAD0:
				keyString = "0";
				break;
			case Keyboard.KEY_NUMPAD1:
				keyString = "1";
				break;
			case Keyboard.KEY_NUMPAD2:
				keyString = "2";
				break;
			case Keyboard.KEY_NUMPAD3:
				keyString = "3";
				break;
			case Keyboard.KEY_NUMPAD4:
				keyString = "4";
				break;
			case Keyboard.KEY_NUMPAD5:
				keyString = "5";
				break;
			case Keyboard.KEY_NUMPAD6:
				keyString = "6";
				break;
			case Keyboard.KEY_NUMPAD7:
				keyString = "7";
				break;
			case Keyboard.KEY_NUMPAD8:
				keyString = "8";
				break;
			case Keyboard.KEY_NUMPAD9:
				keyString = "9";
				break;
			case Keyboard.KEY_A:
				keyString = "a";
				break;
			case Keyboard.KEY_B:
				keyString = "b";
				break;
			case Keyboard.KEY_C:
				keyString = "c";
				break;
			case Keyboard.KEY_D:
				keyString = "d";
				break;
			case Keyboard.KEY_E:
				keyString = "e";
				break;
			case Keyboard.KEY_F:
				keyString = "f";
				break;
			case Keyboard.KEY_G:
				keyString = "g";
				break;
			case Keyboard.KEY_H:
				keyString = "h";
				break;
			case Keyboard.KEY_I:
				keyString = "i";
				break;
			case Keyboard.KEY_J:
				keyString = "j";
				break;
			case Keyboard.KEY_K:
				keyString = "k";
				break;
			case Keyboard.KEY_L:
				keyString = "l";
				break;
			case Keyboard.KEY_M:
				keyString = "m";
				break;
			case Keyboard.KEY_N:
				keyString = "n";
				break;
			case Keyboard.KEY_O:
				keyString = "o";
				break;
			case Keyboard.KEY_P:
				keyString = "p";
				break;
			case Keyboard.KEY_Q:
				keyString = "q";
				break;
			case Keyboard.KEY_R:
				keyString = "r";
				break;
			case Keyboard.KEY_S:
				keyString = "s";
				break;
			case Keyboard.KEY_T:
				keyString = "t";
				break;
			case Keyboard.KEY_U:
				keyString = "u";
				break;
			case Keyboard.KEY_V:
				keyString = "v";
				break;
			case Keyboard.KEY_W:
				keyString = "w";
				break;
			case Keyboard.KEY_X:
				keyString = "x";
				break;
			case Keyboard.KEY_Y:
				keyString = "y";
				break;
			case Keyboard.KEY_Z:
				keyString = "z";
				break;
			case Keyboard.KEY_ADD:
				keyString = "+";
				break;
			case Keyboard.KEY_APOSTROPHE:
				keyString = "'";
				break;
			case Keyboard.KEY_AT:
				keyString = "@";
				break;
			case Keyboard.KEY_BACKSLASH:
				keyString = "\\";
				break;
			case Keyboard.KEY_COLON:
				keyString = ":";
				break;
			case Keyboard.KEY_COMMA:
				keyString = ",";
				break;
			case Keyboard.KEY_DECIMAL:
				keyString = ".";
				break;
			case Keyboard.KEY_BACK:
				keyString = "DEL";
				break;
			case Keyboard.KEY_EQUALS:
				keyString = "=";
				break;
			case Keyboard.KEY_ESCAPE:
				keyString = "ESC";
				break;
			case Keyboard.KEY_GRAVE:
				keyString = "`";
				break;
			case Keyboard.KEY_LBRACKET:
				keyString = "[";
				break;
			case Keyboard.KEY_MINUS:
				keyString = "-";
				break;
			case Keyboard.KEY_MULTIPLY:
				keyString = "*";
				break;		
			case Keyboard.KEY_NUMPADCOMMA:
				keyString = ",";
				break;
			case Keyboard.KEY_NUMPADEQUALS:
				keyString = "=";
				break;
			case Keyboard.KEY_PERIOD:
				keyString = ".";
				break;
			case Keyboard.KEY_POWER:
				keyString = "^";
				break;
			case Keyboard.KEY_RBRACKET:
				keyString ="]";
				break;
			case Keyboard.KEY_SEMICOLON:
				keyString = ";";
				break;
			case Keyboard.KEY_SLASH:
				keyString = "/";
				break;
			case Keyboard.KEY_SPACE:
				keyString = " ";
				break;
			case Keyboard.KEY_SUBTRACT:
				keyString = "-";
				break;
			}	
		}
		else
		{
			switch (key) 
			{
			case Keyboard.KEY_0:
				keyString = ")";
				break;
			case Keyboard.KEY_1:
				keyString = "!";
				break;
			case Keyboard.KEY_2:
				keyString = "@";
				break;
			case Keyboard.KEY_3:
				keyString = "#";
				break;
			case Keyboard.KEY_4:
				keyString = "$";
				break;
			case Keyboard.KEY_5:
				keyString = "%";
				break;
			case Keyboard.KEY_6:
				keyString = "^";
				break;
			case Keyboard.KEY_7:
				keyString = "&";
				break;
			case Keyboard.KEY_8:
				keyString = "*";
				break;
			case Keyboard.KEY_9:
				keyString = "(";
				break;
			case Keyboard.KEY_NUMPAD0:
				keyString = "0";
				break;
			case Keyboard.KEY_NUMPAD1:
				keyString = "1";
				break;
			case Keyboard.KEY_NUMPAD2:
				keyString = "2";
				break;
			case Keyboard.KEY_NUMPAD3:
				keyString = "3";
				break;
			case Keyboard.KEY_NUMPAD4:
				keyString = "4";
				break;
			case Keyboard.KEY_NUMPAD5:
				keyString = "5";
				break;
			case Keyboard.KEY_NUMPAD6:
				keyString = "6";
				break;
			case Keyboard.KEY_NUMPAD7:
				keyString = "7";
				break;
			case Keyboard.KEY_NUMPAD8:
				keyString = "8";
				break;
			case Keyboard.KEY_NUMPAD9:
				keyString = "9";
				break;
			case Keyboard.KEY_A:
				keyString = "A";
				break;
			case Keyboard.KEY_B:
				keyString = "B";
				break;
			case Keyboard.KEY_C:
				keyString = "C";
				break;
			case Keyboard.KEY_D:
				keyString = "D";
				break;
			case Keyboard.KEY_E:
				keyString = "E";
				break;
			case Keyboard.KEY_F:
				keyString = "F";
				break;
			case Keyboard.KEY_G:
				keyString = "G";
				break;
			case Keyboard.KEY_H:
				keyString = "H";
				break;
			case Keyboard.KEY_I:
				keyString = "I";
				break;
			case Keyboard.KEY_J:
				keyString = "J";
				break;
			case Keyboard.KEY_K:
				keyString = "K";
				break;
			case Keyboard.KEY_L:
				keyString = "L";
				break;
			case Keyboard.KEY_M:
				keyString = "M";
				break;
			case Keyboard.KEY_N:
				keyString = "N";
				break;
			case Keyboard.KEY_O:
				keyString = "O";
				break;
			case Keyboard.KEY_P:
				keyString = "P";
				break;
			case Keyboard.KEY_Q:
				keyString = "Q";
				break;
			case Keyboard.KEY_R:
				keyString = "R";
				break;
			case Keyboard.KEY_S:
				keyString = "S";
				break;
			case Keyboard.KEY_T:
				keyString = "T";
				break;
			case Keyboard.KEY_U:
				keyString = "U";
				break;
			case Keyboard.KEY_V:
				keyString = "V";
				break;
			case Keyboard.KEY_W:
				keyString = "W";
				break;
			case Keyboard.KEY_X:
				keyString = "X";
				break;
			case Keyboard.KEY_Y:
				keyString = "Y";
				break;
			case Keyboard.KEY_Z:
				keyString = "Z";
				break;
			case Keyboard.KEY_ADD:
				keyString = "+";
				break;
			case Keyboard.KEY_APOSTROPHE:
				keyString = "\"";
				break;
			case Keyboard.KEY_BACKSLASH:
				keyString = "|";
				break;
			case Keyboard.KEY_COMMA:
				keyString = "<";
				break;
			case Keyboard.KEY_DECIMAL:
				keyString = ">";
				break;
			case Keyboard.KEY_BACK:
				keyString = "DEL";
				break;
			case Keyboard.KEY_EQUALS:
				keyString = "+";
				break;
			case Keyboard.KEY_GRAVE:
				keyString = "~";
				break;
			case Keyboard.KEY_MINUS:
				keyString = "_";
				break;
			case Keyboard.KEY_NUMPADCOMMA:
				keyString = ",";
				break;
			case Keyboard.KEY_NUMPADENTER:
				keyString = "\n";
				break;
			case Keyboard.KEY_NUMPADEQUALS:
				keyString = "=";
				break;
			case Keyboard.KEY_PERIOD:
				keyString = ">";
				break;
			case Keyboard.KEY_POWER:
				keyString = "^";
				break;
			case Keyboard.KEY_LBRACKET:
				keyString = "{";
				break;
			case Keyboard.KEY_RBRACKET:
				keyString = "}";
				break;
			case Keyboard.KEY_RETURN:
				keyString = "\n";
				break;
			case Keyboard.KEY_SEMICOLON:
				keyString = ":";
				break;
			case Keyboard.KEY_SLASH:
				keyString = "?";
				break;
			case Keyboard.KEY_SPACE:
				keyString = " ";
				break;
			case Keyboard.KEY_SUBTRACT:
				keyString = "-";
				break;
			}	
		}

		return keyString;
			
	}
}
