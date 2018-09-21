/*
MEMORY class :
This class has the memory() method which has 3 arguments.
The first argument indicates type of operation performed
whether READ or WRITE.
The second argument holds the effective address.
The third argument is the variable.
This method is initially called by LOADER to
load the instructions into MEMORY.
It is called by CPU to read and write data.
*/
/*
GLOBAL VARIABLES :
'mainMemory' is a two dimensional array which is 256 x 16.
*/

public class MEMORY {
public static char[][] mainMemory=new char[256][16];
public static String memory(String X,int Y,String Z)
{
if(Y<0 || Y>255)
{
        /*To check if memory address out of range - Error*/
        ERROR_HANDLER.Error_Handler(15);
        System.exit(0);
}
switch(X)
{
case "READ":Z="";
                        for(int i=0;i<16;i++)
                        {
                          Z=Z+mainMemory[Y][i];
                        }
                        return Z;
case "WRITE":int k=0;
                         if(Z.length()>=16 && Z.length()%16==0)
                         {
                          for(int i=0;i<Z.length()-1;i+=16)
                         {
                          for(int j=0;j<16;j++)
                          {
                                  mainMemory[Y][j]=Z.charAt(k);
                                  k++;
                          }
                          Y++;
                         }
                         }
}
return "";
}
}
