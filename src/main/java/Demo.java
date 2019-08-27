public class Demo {
    public static void main(String[] args)
    {
        StringBuffer stringBuffer = new StringBuffer("1");

    }

    public int fun()
    {
        int i = 10;
        try
        {
            //doing something

            return i;
        }catch(Exception e){
            return i;
        }finally{
            i = 20;
            System.err.println(i);
        }
    }
}
