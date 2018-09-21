/*
LOADER class :
This class is responsible to load instructions into MEMORY
via loaderBuffer.
The loader() methos is responsible for reading data from
the input file,converting HEX to Binary format and storing
into main memory.
*/
/*
GLOBAL VARIABLES :
'fisrtLine' holds the Job details. Its ID, load address,
initial program counter,size and trace flag in sequence.
'memoryAddress' holds the address in memory to which
instructions will be loaded.
'loaderBuffer' is the Buffer through which instructions
are loaded. It is a two dimensional array which is 4 x 16.
*/

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class LOADER {
public static String firstLine;
public static int memoryAddress;
public static char[][] loaderBuffer=new char[4][16];
/*Method to load 4 words each time into memory via loaderBuffer*/
public static void loader(String X,String Y)
{
        memoryAddress=Integer.parseInt(X,16);
        if(memoryAddress<0 || memoryAddress>255)
        {
                /*check for valid load address - Error*/
                ERROR_HANDLER.Error_Handler(8);
                System.exit(0);
        }
        String file=SYSTEM.inputToLoader;
        BufferedReader reader=null;
        String currentLine=null;
        String word=null;
        String binary=null;
try
{
        reader=new BufferedReader(new FileReader(file));
        firstLine=reader.readLine();
        while((currentLine=reader.readLine())!=null)
        {
        if(currentLine.length()<16 && reader.readLine()!=null)
                {
                        /*check loader format - Error*/
                        ERROR_HANDLER.Error_Handler(13);
                        System.exit(0);
                }
                int row=0;
                int wordIndex=0;
                String loader="";
                while(wordIndex<currentLine.length())
                {
                        word=currentLine.substring(wordIndex,
                          Math.min(wordIndex+4,currentLine.length()));
                        binary=LOADER.hexToBinary(word);
                        wordIndex+=4;
                        for(int column=0;column<16;column++)
                        {
                        loaderBuffer[row][column]=binary.charAt(column);
                        }
                        row++;
                        if(row==4)
                        {
                                for(int i=0;i<4;i++)
                                {
                                for(int j=0;j<16;j++)
                                {
                                loader=loader+loaderBuffer[i][j];
                                }
                                }
                                MEMORY.memory("WRITE",memoryAddress,loader);
                                loaderBuffer=new char[4][16];
                                memoryAddress+=4;
                                if(memoryAddress>255)
                                {
                                        /*Chcek if memory is full - Error*/
                                        ERROR_HANDLER.Error_Handler(20);
                                        System.exit(0);
                                }
                        }
                }
                if(currentLine.length()<16 && reader.readLine()==null)
                {
                        for(int i=0;i<loaderBuffer.length;i++)
                        {
                                for(int j=0;j<loaderBuffer[0].length;j++)
                                {
                                        loader=loader+loaderBuffer[i][j];
                                }
                        }
                        MEMORY.memory("WRITE",memoryAddress,loader);
                }
        }
}
catch (Exception e)
{
        /*check file not found exception - Error*/
        if(e instanceof FileNotFoundException)
        {
                ERROR_HANDLER.Error_Handler(4);
                System.exit(0);
        }
}
}
/*Method to convert HEX to Binary*/
public static String hexToBinary(String hex)
{
        if(!(hex.matches("^[0-9a-fA-F]+$")))
        {
                /*check hex format - Error*/
                ERROR_HANDLER.Error_Handler(12);
                System.exit(0);
        }
        int hexInt=Integer.parseInt(hex,16);
        String binary=Integer.toBinaryString(hexInt);
        int binaryLength=binary.length();
        int requiredlength=hex.length()*4;
        if(binaryLength<requiredlength*4)
        {
                for(int i=0;i<requiredlength-binaryLength;i++)
                {
                        binary="0"+binary;
                }
        }
        return binary;
}
}
