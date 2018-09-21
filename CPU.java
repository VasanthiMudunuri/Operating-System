/*
CPU class :
This class contains the method cpu() that takes the initial
program counter and tracebit as arguments and keeps looping
indefintely until it encounters a HLT instruction or an error.
It has two important methods type0 and type1 to process
respective instruction types.Some methods have been written
to process the instructions easily.
*/
/*
GLOBAL VARIABLES :
'cpuStack' is the stack in CPU which is a two
dimensional array of size 7 x 16.
'topOfStack' is a pointer to point to the top of the stack.
'traceFile' to create a file for trace.
'traceContent' to hold the data to be written into trace file.
'fileWriter' to write into file.
'traceWriter is the buffered writer to write data into the file.
'instructionRegister' to hold the current instruction that is being
executed.
'baseRegister' to hold the starting address in memory where the
instructions have been loaded.
'valueInEffectiveAddress' to hold the value present in the effective address.
'programCounter' to hold the address of the next instruction to be executed.
'effective address is the memory address where data is present.
'trace' to hold tracebit.
'tosValue' to hold the value in top of the stack.
*/

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class CPU {
public static char[][] cpuStack=new char[7][16];
public static int topOfStack=0;
public static File traceFile;
public static String traceContent;
public static FileWriter fileWriter;
public static BufferedWriter traceWriter;
public static String instructionRegister;
public static String baseRegister=SYSTEM.startingAddress;
public static String valueInEffectiveAddress;
public static int programCounter;
public static int effectiveAddress;
public static String trace;
public static String tosValue;
/*cpu() method retrieves the instruction one by one from the
memory, categorizes the instruction into type0 or type1 based
on the instruction type and processes it accordingly. It also
checks for the trace bit. If the tracebit is 1 it writes current
instruction details into trace file.
*/
public static void cpu(String pc,String traceSwitch) throws IOException
{
programCounter=Integer.parseInt(pc,16);
if(programCounter>MEMORY.mainMemory.length)
{
/*check for valid program counter - Error*/
ERROR_HANDLER.Error_Handler(6);
System.exit(0);
}
trace=traceSwitch;
if(trace.equals("1"))
{
traceFile=new File("trace_file.txt");
if(traceFile.exists())
{
        traceFile.delete();
}
traceFile.createNewFile();
fileWriter=new FileWriter(traceFile,true);
traceWriter=new BufferedWriter(fileWriter);
traceWriter.write(String.format("%s%6s%6s%7s%6s%4s%7s%6s%6s%4s%7s",
  "PC","BR","IR","TOS"," S[TOS]","EA","(EA)","TOS"," S[TOS]","EA","(EA)"));
traceWriter.newLine();
traceWriter.write(String.format("%s%6s%6s%6s%6s%6s%6s%6s%6s%6s%6s",
  "HEX","HEX","HEX","HEX","HEX","HEX","HEX","HEX","HEX","HEX","HEX"));
traceWriter.newLine();
}
while(true)
{
         /*The loop is set to run indefintely until it reaches halt*/
        instructionRegister=MEMORY.memory("READ",programCounter,instructionRegister);
        if(trace.equals("1"))
        {
        String content=Integer.toHexString(programCounter).toUpperCase();
                        while(content.length()<2)
            {
              content="0"+content;
            }
                String content1=Integer.toHexString(Integer.parseInt(instructionRegister,2))
                                         .toUpperCase();
                        while(content1.length()<4)
            {
              content1="0"+content1;
            }
                traceContent=content+" "+
                                         baseRegister.toUpperCase()+" "+
                                         content1;
        }
        if(SYSTEM.clock>100000)
        {
        /*To terminate long running process considering infinite loop - Error*/
                ERROR_HANDLER.Error_Handler(16);
                System.exit(0);
        }
        programCounter+=1;
        if(instructionRegister.charAt(0)=='1')
        {
                SYSTEM.clock+=4; /*clock increment for type1 instruction*/
                type1(instructionRegister);
        }
        /*To check if there are two type0 instructions together*/
        else if(instructionRegister.charAt(0)=='0'
        && instructionRegister.charAt(8)=='0' &&
        (Integer.parseInt(instructionRegister.substring(0,8)))!=0)
        {
                /*To check if first instruction is RTN so that no operation
                which is padded will not be executed and no clock change occurs*/
                if(Integer.parseInt(instructionRegister.substring(0,8))!=21)
                {
                        SYSTEM.clock+=2;/*For two type0 instructions*/
                        type0("00000000"+instructionRegister.substring(0,8));
                        String content=Integer.toHexString(programCounter).toUpperCase();
                        while(content.length()<2)
            {
              content="0"+content;
            }
                        String content1=Integer.toHexString(Integer.parseInt(instructionRegister,2))
                                         .toUpperCase();
                        while(content1.length()<4)
            {
              content1="0"+content1;
            }
                        traceContent=content+" "+
                                         baseRegister.toUpperCase()+" "+content1;
                        type0("00000000"+instructionRegister.substring(8,16));
                }
                else
                {
                        SYSTEM.clock+=1;/*For RTN*/
                        type0("00000000"+instructionRegister.substring(0,8));
                        String content=Integer.toHexString(programCounter).toUpperCase();
                        while(content.length()<2)
            {
              content="0"+content;
            }
                        String content1=Integer.toHexString(Integer.parseInt(instructionRegister,2))
                                         .toUpperCase();
                        while(content1.length()<4)
            {
              content1="0"+content1;
            }
                        traceContent=content+" "+
                                         baseRegister.toUpperCase()+" "+content1;
                        type0("00000000"+instructionRegister.substring(8,16));
                }
        }
        /**/
        else
        {
                SYSTEM.clock+=2; /*clock increment for type0 instruction one for
                                  no operation and one for instruction*/
                type0(instructionRegister);
        }
}
}
/*To convert Binary into decimal*/
public static int twoComplementDecimalValue(String binary)
{
if(binary.charAt(0)=='1' && binary.length()==16)
{
        binary=binary.replace('0','#');
        binary=binary.replace('1','0');
        binary=binary.replace('#','1');
        int decimalValue=(Integer.parseInt(binary,2)+1)*-1;
        return decimalValue;
}
return Integer.parseInt(binary,2);
}
/*To get the value at a particular index in stack*/
public static String getStackValue(int index)
{
String value="";
for(int i=0;i<16;i++)
{
        value=value+cpuStack[index][i];
}
return value;
}
/*To save value into the stack*/
public static void saveToStack(int index,String value)
{
for(int k=0;k<16;k++)
{
        cpuStack[index][k]=value.charAt(k);
}
}
/*To pad with zero's in case the binary value is not 16 bit*/
public static String zeroPadding(String binary)
{
int length=binary.length();
if(length!=16)
{
 for(int j=0;j<16-length;j++)
        {
                binary="0"+binary;
        }
}
return binary;
}
/*To process type0 instructions*/
public static void type0(String instruction) throws IOException
{
String opcode=instruction.substring(11,16);
String belowtosValue;
String Result;
if(trace.equals("1"))
{
tosValue=getStackValue(topOfStack);
if(topOfStack==0)
{
        tosValue="0";
}
String content1=Integer.toHexString(Integer.parseInt(tosValue,2)).toUpperCase();
                        while(content1.length()<2)
            {
              content1="0"+content1;
            }
traceContent=traceContent+" "+Integer.toHexString(topOfStack)
             .toUpperCase()+" "+content1;
}
switch(opcode)
{
case "00000":break;
case "00001":tosValue=getStackValue(topOfStack);
                 belowtosValue=getStackValue(topOfStack-1);
                 Result=zeroPadding(Integer.toBinaryString
                 (twoComplementDecimalValue(tosValue)|twoComplementDecimalValue(belowtosValue)));
                 saveToStack(topOfStack-1,Result);
                 topOfStack-=1;
                 break;
case "00010":tosValue=getStackValue(topOfStack);
                 belowtosValue=getStackValue(topOfStack-1);
                 Result=zeroPadding(Integer.toBinaryString
                 (twoComplementDecimalValue(tosValue)&twoComplementDecimalValue(belowtosValue)));
                 saveToStack(topOfStack-1,Result);
                 topOfStack-=1;
                 break;
case "00011":tosValue=getStackValue(topOfStack);
                 Result=zeroPadding(Integer.toBinaryString(~(twoComplementDecimalValue(tosValue))));
                 saveToStack(topOfStack,Result);
                 break;
case "00100":tosValue=getStackValue(topOfStack);
                 belowtosValue=getStackValue(topOfStack-1);
                 Result=zeroPadding(Integer.toBinaryString
                 (twoComplementDecimalValue(tosValue)^twoComplementDecimalValue(belowtosValue)));
                 saveToStack(topOfStack-1,Result);
                 topOfStack-=1;
                 break;
case "00101":tosValue=getStackValue(topOfStack);
                 belowtosValue=getStackValue(topOfStack-1);
                 Result=zeroPadding(Integer.toBinaryString
                 (twoComplementDecimalValue(tosValue)+twoComplementDecimalValue(belowtosValue)));
                 saveToStack(topOfStack-1,Result);
                 topOfStack-=1;
                 break;
case "00110":tosValue=getStackValue(topOfStack);
                 belowtosValue=getStackValue(topOfStack-1);
                 Result=zeroPadding(Integer.toBinaryString
                 (twoComplementDecimalValue(belowtosValue)-twoComplementDecimalValue(tosValue)));
                 saveToStack(topOfStack-1,Result);
                 topOfStack-=1;
                 break;
case "00111":tosValue=getStackValue(topOfStack);
                 belowtosValue=getStackValue(topOfStack-1);
                 Result=zeroPadding(Integer.toBinaryString
                 (twoComplementDecimalValue(tosValue)*twoComplementDecimalValue(belowtosValue)));
                 saveToStack(topOfStack-1,Result);
                 topOfStack-=1;
                 break;
case "01000":tosValue=getStackValue(topOfStack);
                 belowtosValue=getStackValue(topOfStack-1);
                 Result=zeroPadding(Integer.toBinaryString
                 (twoComplementDecimalValue(belowtosValue)/twoComplementDecimalValue(tosValue)));
                 saveToStack(topOfStack-1,Result);
                 topOfStack-=1;
                 break;
case "01001":tosValue=getStackValue(topOfStack);
                 belowtosValue=getStackValue(topOfStack-1);
                 Result=zeroPadding(Integer.toBinaryString
                 (twoComplementDecimalValue(belowtosValue)%twoComplementDecimalValue(tosValue)));
                 saveToStack(topOfStack-1,Result);
                 topOfStack-=1;
                 break;
case "01010":tosValue=getStackValue(topOfStack);
                 Result=zeroPadding(tosValue.substring(1,16)+"0");
                 saveToStack(topOfStack,Result);
                 break;
case "01011":tosValue=getStackValue(topOfStack);
                 Result=zeroPadding(Integer.toBinaryString
                 (twoComplementDecimalValue(tosValue) >>> 1));
                 saveToStack(topOfStack,Result);
                 break;
case "01100":tosValue=getStackValue(topOfStack);
                 belowtosValue=getStackValue(topOfStack-1);
                 /*True value is assumed to be 1 and for False it is 0*/
                 if(twoComplementDecimalValue(belowtosValue)
                         >twoComplementDecimalValue(tosValue))
                 {
                         saveToStack(topOfStack+1,"0000000000000001");
                 }
                 else
                 {
                         saveToStack(topOfStack+1,"0000000000000000");
                 }
                 topOfStack+=1;
                 break;
case "01101":tosValue=getStackValue(topOfStack);
                 belowtosValue=getStackValue(topOfStack-1);
                 if(twoComplementDecimalValue(belowtosValue)
                         <twoComplementDecimalValue(tosValue))
                 {
                         saveToStack(topOfStack+1,"0000000000000001");
                 }
                 else
                 {
                         saveToStack(topOfStack+1,"0000000000000000");
                 }
                 topOfStack+=1;
                 break;
case "01110":tosValue=getStackValue(topOfStack);
                 belowtosValue=getStackValue(topOfStack-1);
                 if(twoComplementDecimalValue(belowtosValue)
                         ==twoComplementDecimalValue(tosValue))
                 {
                         saveToStack(topOfStack+1,"0000000000000001");
                 }
                 else
                 {
                         saveToStack(topOfStack+1,"0000000000000000");
                 }
                 topOfStack+=1;
                 break;
case "01111":break;
case "10000":break;
case "10001":break;
case "10010":break;
case "10011":SYSTEM.clock+=15;
                 SYSTEM.iotime+=15;
                 topOfStack+=1;
                 Scanner reader=new Scanner(System.in);
                 try
                 {
                 int input=reader.nextInt();
                 if(input<-8192 || input>8191)
                 {
                         /*check range of integer value - Error*/
                         ERROR_HANDLER.Error_Handler(2);
                         System.exit(0);
                 }
                 else
                 {
                         if(input>0)
                         {
                                 saveToStack(topOfStack,zeroPadding(Integer.toBinaryString(input)));
                         }
                         else
                         {
                                 String twoComplement=Integer.toBinaryString(input);
                                 saveToStack(topOfStack,twoComplement.substring
                                 (twoComplement.length()-16,twoComplement.length()));
                         }
                 }
                 }
                 catch(Exception e)
                 {
                         /*check for invalid input like characters - Error*/
                         ERROR_HANDLER.Error_Handler(3);
                         System.exit(0);
                 }
                 break;
case "10100":SYSTEM.clock+=15;
                     SYSTEM.iotime+=15;
                     SYSTEM.result=SYSTEM.result+" "+getStackValue(topOfStack)+" ";
                 System.out.println(getStackValue(topOfStack));
                     topOfStack-=1;
                     break;
case "10101":programCounter=Integer.parseInt(getStackValue(topOfStack),2);
                 topOfStack-=1;
                 break;
case "10110":break;
case "10111":break;
case "11000":/*To generate output file upon halt*/
                 try
                 {
                File output=new File("output_file.txt");
                if(output.exists())
                {
                        output.delete();
                }
                output.createNewFile();
                FileWriter fileWriter=new FileWriter(output);
                BufferedWriter outputWriter=new BufferedWriter(fileWriter);
                outputWriter.write("JOB ID            :\t"+SYSTEM.jobId+"(HEX)");
                outputWriter.newLine();
                outputWriter.write("Warning Message   :\t"+SYSTEM.errorMessage);
                outputWriter.newLine();
                outputWriter.write("Job Termination   :\t"+SYSTEM.terminatingMessage);
                outputWriter.newLine();
                outputWriter.write("Program Output    :\t"+SYSTEM.result+"(BINARY)");
                outputWriter.newLine();
                outputWriter.write("System Clock      :\t"+SYSTEM.clock+"(DECIMAL)");
                outputWriter.newLine();
                outputWriter.write("Input/Output time :\t"+SYSTEM.iotime+"(DECIMAL)");
                outputWriter.newLine();
                outputWriter.write("Execution time    :\t"+(SYSTEM.clock-SYSTEM.iotime)
                 +"(DECIMAL)");
                outputWriter.close();
                 }
                 catch(Exception e)
                 {
                         /*check output file creation - Error*/
                         ERROR_HANDLER.Error_Handler(21);
                         System.exit(0);
                 }
                 if(trace.equals("1"))
                 {
                         traceWriter.close();
                 }
                 System.exit(0);
 default:/*check opcode - Error*/
                 ERROR_HANDLER.Error_Handler(14);
                 System.exit(0);
                 break;
}
if(trace.equals("1"))
{
String[] traceOut=traceContent.split("\\s+");
for(String s : traceOut)
{
traceWriter.write(String.format("%-6s",s));
}
String content=Integer.toHexString(Integer.parseInt(getStackValue(topOfStack),2)).toUpperCase();
                        while(content.length()<2)
            {
              content="0"+content;
            }
traceContent=String.format("%13s     %s",Integer.toHexString(topOfStack).toUpperCase(),
content);
traceWriter.write(traceContent);
traceWriter.newLine();
}
}
/*To process type1 instructions*/
public static void type1(String instruction) throws IOException
{
String opcode=instruction.substring(1,6);
char index=instruction.charAt(6);
int displacementAddress=Integer.parseInt(instruction.substring(9,16),2);
String memoryValue;
String Result;
if(index=='1')
{
        effectiveAddress=displacementAddress+Integer.parseInt(getStackValue(topOfStack),2);
}
else
{
        effectiveAddress=displacementAddress;
}
if(effectiveAddress>MEMORY.mainMemory.length)
{
        /*check for valid effective address - Error*/
        ERROR_HANDLER.Error_Handler(19);
        System.exit(0);
}
if(trace.equals("1"))
{
        tosValue=getStackValue(topOfStack);
        if(topOfStack==0)
        {
                tosValue="0";
        }
        memoryValue=MEMORY.memory("READ",effectiveAddress,valueInEffectiveAddress);
        String content=Integer.toHexString(Integer.parseInt(tosValue,2)).toUpperCase();
                        while(content.length()<2)
            {
              content="0"+content;
            }
        String content1=Integer.toHexString(effectiveAddress).toUpperCase();
                        while(content1.length()<2)
            {
              content1="0"+content1;
            }
        String content2=Integer.toHexString(Integer.parseInt(memoryValue,2)).toUpperCase();
                        while(content2.length()<4)
            {
              content2="0"+content2;
            }
        traceContent=traceContent+" "+Integer.toHexString(topOfStack).toUpperCase()+" "+
                         content+" "+
                         content1+" "+content2;
}
switch(opcode)
{
case "00000":break;
case "00001":memoryValue=MEMORY.memory("READ",effectiveAddress,valueInEffectiveAddress);
                         tosValue=getStackValue(topOfStack);
                         Result=zeroPadding(Integer.toBinaryString
                         (twoComplementDecimalValue(tosValue)|twoComplementDecimalValue(memoryValue)));
                         saveToStack(topOfStack,Result);
                         break;
case "00010":memoryValue=MEMORY.memory("READ",effectiveAddress,valueInEffectiveAddress);
                         tosValue=getStackValue(topOfStack);
                         Result=zeroPadding(Integer.toBinaryString
                         (twoComplementDecimalValue(tosValue)&twoComplementDecimalValue(memoryValue)));
                         saveToStack(topOfStack,Result);
                         break;
case "00011":break;
case "00100":memoryValue=MEMORY.memory("READ",effectiveAddress,valueInEffectiveAddress);
                         tosValue=getStackValue(topOfStack);
                         Result=zeroPadding(Integer.toBinaryString
                         (twoComplementDecimalValue(tosValue)^twoComplementDecimalValue(memoryValue)));
                         saveToStack(topOfStack,Result);
                         break;
case "00101":memoryValue=MEMORY.memory("READ",effectiveAddress,valueInEffectiveAddress);
                         tosValue=getStackValue(topOfStack);
                         Result=zeroPadding(Integer.toBinaryString
                         (twoComplementDecimalValue(tosValue)+twoComplementDecimalValue(memoryValue)));
                         saveToStack(topOfStack,Result);
                         break;
case "00110":memoryValue=MEMORY.memory("READ",effectiveAddress,valueInEffectiveAddress);
                         tosValue=getStackValue(topOfStack);
                         Result=zeroPadding(Integer.toBinaryString
                         (twoComplementDecimalValue(tosValue)-twoComplementDecimalValue(memoryValue)));
                         saveToStack(topOfStack,Result);
                         break;
case "00111":memoryValue=MEMORY.memory("READ",effectiveAddress,valueInEffectiveAddress);
                         tosValue=getStackValue(topOfStack);
                         Result=zeroPadding(Integer.toBinaryString
                         (twoComplementDecimalValue(tosValue)*twoComplementDecimalValue(memoryValue)));
                         saveToStack(topOfStack,Result);
                         break;
case "01000":memoryValue=MEMORY.memory("READ",effectiveAddress,valueInEffectiveAddress);
                         tosValue=getStackValue(topOfStack);
                         Result=zeroPadding(Integer.toBinaryString
                         (twoComplementDecimalValue(tosValue)/twoComplementDecimalValue(memoryValue)));
                         saveToStack(topOfStack,Result);
                         break;
case "01001":memoryValue=MEMORY.memory("READ",effectiveAddress,valueInEffectiveAddress);
                         tosValue=getStackValue(topOfStack);
                         Result=zeroPadding(Integer.toBinaryString
                         (twoComplementDecimalValue(tosValue)%twoComplementDecimalValue(memoryValue)));
                         saveToStack(topOfStack,Result);
                         break;
case "01010":break;
case "01011":break;
case "01100":memoryValue=MEMORY.memory("READ",effectiveAddress,valueInEffectiveAddress);
                         tosValue=getStackValue(topOfStack);
                         /*True value is assumed to be 1 and false value to 0*/
                         if(twoComplementDecimalValue(tosValue)>twoComplementDecimalValue(memoryValue))
                         {
                                 saveToStack(topOfStack+1,"0000000000000001");
                         }
                         else
                         {
                                 saveToStack(topOfStack+1,"0000000000000000");
                         }
                         topOfStack+=1;
                         break;
case "01101":memoryValue=MEMORY.memory("READ",effectiveAddress,valueInEffectiveAddress);
                         tosValue=getStackValue(topOfStack);
                         if(twoComplementDecimalValue(tosValue)<twoComplementDecimalValue(memoryValue))
                         {
                                 saveToStack(topOfStack+1,"0000000000000001");
                         }
                         else
                         {
                                 saveToStack(topOfStack+1,"0000000000000000");
                         }
                         topOfStack+=1;
                         break;
case "01110":memoryValue=MEMORY.memory("READ",effectiveAddress,valueInEffectiveAddress);
                         tosValue=getStackValue(topOfStack);
                         if(twoComplementDecimalValue(tosValue)==twoComplementDecimalValue(memoryValue))
                         {
                                 saveToStack(topOfStack+1,"0000000000000001");
                         }
                         else
                         {
                                 saveToStack(topOfStack+1,"0000000000000000");
                         }
                         topOfStack+=1;
                         break;
case "01111":programCounter=effectiveAddress;
                         break;
case "10000":if(getStackValue(topOfStack).equals("0000000000000001"))
                         {
                           programCounter=effectiveAddress;
                         }
                         topOfStack-=1;
                         break;
case "10001":if(getStackValue(topOfStack).equals("0000000000000000"))
                         {
                           programCounter=effectiveAddress;
                         }
                         topOfStack-=1;
                         break;
case "10010":topOfStack+=1;
                         saveToStack(topOfStack,zeroPadding(Integer.toBinaryString(programCounter)));
                         programCounter=effectiveAddress;
                         break;
case "10011":break;
case "10100":break;
case "10101":break;
case "10110":topOfStack+=1;
                         if(topOfStack==8)
                         {
                                 /*check if stack is full to push - Error*/
                                ERROR_HANDLER.Error_Handler(18);
                                System.exit(0);
                         }
                         memoryValue=MEMORY.memory("READ",effectiveAddress,valueInEffectiveAddress);
                         saveToStack(topOfStack,zeroPadding(memoryValue));
                         break;
case "10111":if(topOfStack==0)
                         {
                                /*check if stack is empty - Error*/
                                ERROR_HANDLER.Error_Handler(17);
                                System.exit(0);
                         }
                         else
                         {
                                 tosValue=getStackValue(topOfStack);
                                 int length=tosValue.length();
                                 if(length!=16)
                                 {
                                         for(int i=0;i<16-length;i++)
                                         {
                                                 tosValue="0"+tosValue;
                                         }
                                 }
                                 MEMORY.memory("WRITE",effectiveAddress,tosValue);
                                 topOfStack-=1;
                         }
                         break;
case "11000":break;
         default:/*check opcode - Error*/
                         ERROR_HANDLER.Error_Handler(14);
                         System.exit(0);
                         break;
}
if(trace.equals("1"))
{
        tosValue=getStackValue(topOfStack);
        if(topOfStack==0)
        {
                tosValue="0";
        }
        memoryValue=MEMORY.memory("READ",effectiveAddress,valueInEffectiveAddress);
        String content=Integer.toHexString(Integer.parseInt(tosValue,2)).toUpperCase();
                        while(content.length()<2)
            {
              content="0"+content;
            }
        String content1=Integer.toHexString(effectiveAddress).toUpperCase();
                        while(content1.length()<2)
            {
              content1="0"+content1;
            }
        String content2=Integer.toHexString(Integer.parseInt(memoryValue,2)).toUpperCase();
                        while(content2.length()<4)
            {
              content2="0"+content2;
            }
        traceContent=traceContent+" "+Integer.toHexString(topOfStack).toUpperCase()+" "+
                                 content+" "+
                                 content1+" "+content2;
        String[] traceOut=traceContent.split("\\s+");
        for(String s : traceOut)
        {
        traceWriter.write(String.format("%-6s",s));
        }
        traceWriter.newLine();
}
}
}
