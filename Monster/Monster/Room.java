public class Room
{
    private int size;
    public Room()
    {
        int rand=(int)(Math.random()*10);
        if(rand==0)
        {
            size=11;
        }
        if(rand==1)
        {
            size=7;
        }
        if(rand==2)
        {
            size=7;
        }
        if(rand==3)
        {
            size=9;
        }
        if(rand==4)
        {
            size=9;
        }
        if(rand==5)
        {
            size=9;
        }
        if(rand==6)
        {
            size=9;
        }
        if(rand==7)
        {
            size=11;
        }
        if(rand==8)
        {
            size=11;
        }
        if(rand==9)
        {
            rand=(int)(Math.random()*10);
            if(rand==9)
            {
                size=21;
            }
            else
            {
                size=9;
            }
        }
    }
    public int getIntSize()
    {
        if(size==11||size==21)
        {
            return size;
        }
        return size;
    }
    public String getSize()
    {
        if(size==11||size==21)
        {
            return ""+size;
        }
        return " "+size;
    }
    public String toString()
    {
        String[][] sa=new String[11][11];
        for(int i=0;i<11;i++)
        {
            for(int j=0;j<11;j++)
            {
                if(getIntSize()>i&&getIntSize()>j)
                {
                    sa[i][j]="#";
                }
                else
                {
                    sa[i][j]=" ";
                }
                if(i==5||j==5)
                {
                    sa[i][j]="#";
                }
            }
        }
        String s="";
        for(int i=0;i<sa.length;i++)
        {
            for(int j=0;j<sa[0].length;j++)
            {
                s+=sa[i][j];
            }
            s+="\n";
        }
        return s;
    }
}
