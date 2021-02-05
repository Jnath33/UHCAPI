package fr.jnath.UHCAPI.tchat;

public class TchatFormat {

    private TchatContentType[] tchatContentTypes = new TchatContentType[6];
    private int iterator = 0;

    public void setNext(TchatContentType tchatContentType){
        tchatContentTypes[iterator]=tchatContentType;
        iterator++;
    }

    public String generate(String message, String tag, String playerName, String otherOne, String otherTwo, String otherThree){
        StringBuilder strBuilder = new StringBuilder("");
        for(TchatContentType tchatContentType : tchatContentTypes){
            if(tchatContentType == null) break;
            switch (tchatContentType){
                case MESSAGE:
                    strBuilder.append(message+" ");
                    System.out.println("test");
                    break;
                case TAG:
                    strBuilder.append(tag+" ");
                    System.out.println("test2");
                    break;
                case PLAYER:
                    strBuilder.append(playerName+" ");
                    System.out.println("test3");
                    break;
                case OTHER_1:
                    strBuilder.append(otherOne+" ");
                    System.out.println("test4");
                    break;
                case OTHER_2:
                    strBuilder.append(otherTwo+" ");
                    System.out.println("test5");
                    break;
                case OTHER_3:
                    strBuilder.append(otherThree+" ");
                    System.out.println("test6");
                    break;
                default:
                    System.out.println("test7");
                    return strBuilder.toString();
            }
        }
        System.out.println("test8");
        return strBuilder.toString();
    }
}
