package fr.jnath.UHCAPI.tchat;

public class TchatFormat {

    private final TchatContentType[] tchatContentTypes = new TchatContentType[6];
    private int iterator = 0;

    public void setNext(TchatContentType tchatContentType){
        tchatContentTypes[iterator]=tchatContentType;
        iterator++;
    }

    public void endAll(){
        while (iterator<6){
            tchatContentTypes[iterator]=null;
            iterator++;
        }
    }

    public String generate(String message, String tag, String playerName, String otherOne, String otherTwo, String otherThree){
        StringBuilder strBuilder = new StringBuilder("");
        for(int i=0;i<6;i++){
            System.out.println(tchatContentTypes[i]);
            switch (tchatContentTypes[i]){
                case MESSAGE:
                    strBuilder.append(message+" ");
                    break;
                case TAG:
                    strBuilder.append(tag+" ");
                    break;
                case PLAYER:
                    strBuilder.append(playerName+" ");
                    break;
                case OTHER_1:
                    strBuilder.append(otherOne+" ");
                    break;
                case OTHER_2:
                    strBuilder.append(otherTwo+" ");
                    break;
                case OTHER_3:
                    strBuilder.append(otherThree+" ");
                    break;
                default:
                    break;
            }
        }
        return strBuilder.toString();
    }
}
