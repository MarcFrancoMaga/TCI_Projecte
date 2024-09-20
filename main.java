//import Functions;

public class Main
{
    public static void main(String[] args)
    {
        if(args.length < 6)
        {
            System.out.prinln("Insuficient arguments!!");
            return;
        }

        String path_file = args[0];
        int rows = Integer.parseInt(args[1]);
        int columns= Integer.parseInt(args[2]);
        int components= Integer.parseInt(args[3]);
        int bytes_sample= Integer.parseInt(args[4]);
        boolean signed= Boolean.parseBoolean(args[5]);

        int [][][] image = Functions.LoadImage(path_file,rows,columns,components,bytes_sample,signed);


    }
}