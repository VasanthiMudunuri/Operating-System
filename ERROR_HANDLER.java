/*
ERROR_HANDLER class :
This class handles all the warnings and errors that occur
during execution.
In case of a warning, it daiplays the message and continues
execution and
in case of an error,it dispalys the message and halts
the execution.
*/
/*
cases include error and warning messages.
*/

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class ERROR_HANDLER {

public static void Error_Handler(int error)
{
        switch(error)
        {
        case 1 :SYSTEM.terminatingMessage="Warning Message";
                SYSTEM.errorMessage="Job Id is not provided, "
                        +"by default 1 is given";
                break;
        case 2 :SYSTEM.terminatingMessage="Abnormal Termination";
                    SYSTEM.errorMessage="Integer value out of range "
                                        +"(-2^13 to (2^13)-1)";
                        break;
        case 3 :SYSTEM.terminatingMessage="Abnormal Termination";
                    SYSTEM.errorMessage="Invalid input. Only integers in "
                +"range(-2^13 to (2^13)-1) are supported";
                        break;
        case 4 :SYSTEM.terminatingMessage="Abnormal Termination";
                        SYSTEM.errorMessage="Input file not found";
                        break;
        case 5 :SYSTEM.terminatingMessage="Abnormal Termination";
                        SYSTEM.errorMessage="Initial program counter "
                                        +"not provided";
                        break;
        case 6 :SYSTEM.terminatingMessage="Abnormal Termination";
                        SYSTEM.errorMessage="Invalid program counter";
                        break;
        case 7 :SYSTEM.terminatingMessage="Abnormal Termination";
                        SYSTEM.errorMessage="load address dosen't exist";
                        break;
        case 8 :SYSTEM.terminatingMessage="Abnormal Termination";
                        SYSTEM.errorMessage="Invalid load address";
                        break;
        case 9 :SYSTEM.terminatingMessage="Warning Message";
                        SYSTEM.errorMessage="Program size is not specified";
                        break;
        case 10:SYSTEM.terminatingMessage="Warning Message";
                        SYSTEM.errorMessage="Trace bit not specified,"
                                        +"by default 0 is given";
                        break;
        case 11:SYSTEM.terminatingMessage="Warning Message";
                        SYSTEM.errorMessage="Bad Trace flag";
                        break;
        case 12:SYSTEM.terminatingMessage="Abnormal Termination";
                        SYSTEM.errorMessage="Invalid hex format";
                        break;
        case 13:SYSTEM.terminatingMessage="Abnormal Termination";
                        SYSTEM.errorMessage="Invalid loader format";
                        break;
        case 14:SYSTEM.terminatingMessage="Abnormal Termination";
                        SYSTEM.errorMessage="Invalid opcode";
                        break;
        case 15:SYSTEM.terminatingMessage="Abnormal Termination";
                        SYSTEM.errorMessage="Memory location out of range";
                        break;
        case 16:SYSTEM.terminatingMessage="Abnormal Termination";
                SYSTEM.errorMessage="Execution entered into infinite loop";
                        break;
        case 17:SYSTEM.terminatingMessage="Abnormal Termination";
                        SYSTEM.errorMessage="Stack is empty to pop";
                        break;
        case 18:SYSTEM.terminatingMessage="Abnormal Termination";
                        SYSTEM.errorMessage="Stack is full to push";
                        break;
        case 19:SYSTEM.terminatingMessage="Abnormal Termination";
                        SYSTEM.errorMessage="Invalid effective address";
                        break;
        case 20:SYSTEM.terminatingMessage="Abnormal Termination";
                        SYSTEM.errorMessage="Memory is full";
                        break;
        case 21:SYSTEM.terminatingMessage="Abnormal Termination";
                        SYSTEM.errorMessage="Output path does not exist";
                        break;
        case 22:SYSTEM.terminatingMessage="Abnormal Termination";
                        SYSTEM.errorMessage="Program size is huge."
                                        +"Memory is not sufficient";
                        break;
        }
        /*Writing output to a file*/
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
   outputWriter.write("Error Message     :\t"+SYSTEM.errorMessage);
   outputWriter.newLine();
   outputWriter.write("Job Termination   :\t"+SYSTEM.terminatingMessage);
   outputWriter.newLine();
   outputWriter.write("Program Output    :\t"+SYSTEM.result+"(BINARY)");
   outputWriter.newLine();
   outputWriter.write("System Clock      :\t"+SYSTEM.clock+"(DECIMAL)");
   outputWriter.newLine();
   outputWriter.write("Input/Output time :\t"+SYSTEM.iotime+"(DECIMAL)");
   outputWriter.newLine();
   outputWriter.write("Execution time    :\t"+
                 (SYSTEM.clock-SYSTEM.iotime)+"(DECIMAL)");
   outputWriter.close();
   }
   catch(Exception e)
   {
          ERROR_HANDLER.Error_Handler(21);
  System.exit(0);
   }
}
}
