/*
SYSTEM class :
It is the main class that has the control of execution of entire project.
It reads the input file line by line, executes all the instructions and
returns the output for an entered input.
It has been implemented according to the specifiactions given.
*/
/*
GLOBAL VARIABLES :
'inputToLoader' containes path of the input file passed as an argument
which contains instructions in HEX.
'startingAddress' is the starting address indicating where the current
job should be loaded.
'traceSwitch' is the trace flag.
'jodId' is the current job ID.
'initialProgramCounter' is the initial instruction of the current job
to be executed.
'jobSize' is the size of the current job.
'clock' is the System clock.
'iotime' is the input/ouput time.
'errorMessage' is the message to be displayed in case of an error.
'terminatingMessage' is the message indicating type of job termination
whether abnormal or normal.
'result' stores the output of the current job.
*/

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;

public class SYSTEM {
public static String inputToLoader="";
public static String startingAddress="";
public static String traceSwitch="";
public static String jobId="";
public static String initialProgramCounter="";
public static String jobSize="";
public static int clock=0;
public static int iotime=0;
public static String errorMessage="Nothing to display";
public static String terminatingMessage="Normal Termination";
public static String result="";

public static void main(String[] args) throws Exception
{
        SYSTEM.inputToLoader=args[0];
        try
        {
                BufferedReader bufferedReader=new BufferedReader
                                (new FileReader(SYSTEM.inputToLoader));
                String firstLine=bufferedReader.readLine();
                String[] jobDetails=firstLine.split(" ");
                jobId=jobDetails[0];
                if(jobId.isEmpty())
                {
                        /*check for job id - Warning*/
                        ERROR_HANDLER.Error_Handler(1);
                        jobId="01";
                }
                startingAddress=jobDetails[1];
                if(startingAddress.isEmpty())
                {
                        /*chcek for load address - Error*/
                        ERROR_HANDLER.Error_Handler(7);
                        System.exit(0);
                }
                initialProgramCounter=jobDetails[2];
                if(initialProgramCounter.isEmpty())
                {
                        /*check for initial program counter - Error*/
                        ERROR_HANDLER.Error_Handler(5);
                        System.exit(0);
                }
                jobSize=jobDetails[3];
                if(jobSize.isEmpty())
                {
                        /*check for job size - Warning*/
                        ERROR_HANDLER.Error_Handler(9);
                }
                if(Integer.parseInt(jobSize,16)>256)
                {
                        /*check if job size is very big
                        that cannot fit into memory - Error*/
                        ERROR_HANDLER.Error_Handler(22);
                        System.exit(0);
                }
                traceSwitch=jobDetails[4];
                if(traceSwitch.isEmpty())
                {
                        /*check for trace bit - Warning , default
                        taken 0*/
                        ERROR_HANDLER.Error_Handler(10);
                        traceSwitch="0";
                }
                if(!(traceSwitch.equals("1") || traceSwitch.equals("0")))
                {
                        /*check for bad tracebit - Warning*/
                        ERROR_HANDLER.Error_Handler(11);
                }
                if(traceSwitch.equals("0"))
                {
                  File traceFile=new File("trace_file.txt");
                  if(traceFile.exists())
                   {
                   traceFile.delete();
                }
                traceFile.createNewFile();
                }
                bufferedReader.close();
        }
        catch (Exception e)
        {
                /*File not found exception - Error*/
                if(e instanceof FileNotFoundException)
                {
                        ERROR_HANDLER.Error_Handler(4);
                        System.exit(0);
                }
        }
        LOADER.loader(startingAddress,traceSwitch);
        CPU.cpu(initialProgramCounter, traceSwitch);
}
}
