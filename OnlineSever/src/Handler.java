
import java.io.*;
import java.net.Socket;
import java.util.List;


/**
 * @author qingzhengyu1111@outlook.com
 * @date 2023/5/29 18:48
 */
public class Handler extends Thread {
    private List<Socket> mList;
    private BufferedReader msgFromPlayer1 = null;
    private BufferedReader msgFromPlayer2 = null;

    private String msg1 = "";

    private String msg2 = "";

    private String sendmsg = "";

    private int score1 = 0;

    private int score2 = 0;

    private volatile boolean flag1 = false;

    private volatile boolean flag2 = false;


    public Handler(List<Socket> mList) {
        //客户端
        this.mList = mList;
        try {
            msgFromPlayer1 = new BufferedReader(
                    new InputStreamReader(mList.get(0).getInputStream(), "UTF-8"));

            msgFromPlayer2 = new BufferedReader(
                    new InputStreamReader(mList.get(1).getInputStream(), "UTF-8"));

            sendmsg = "start";   //已经为你匹配到了对手，对战即将开始

            this.sendmsg();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        new Thread(() -> {
            try {
                while ((msg1 = msgFromPlayer1.readLine()) != null) {
                    if (msg1.equals("end")) {
                        System.out.println("玩家1已死亡");
                        flag1 = true;
                        break;
                    } else {
                        score1 = Integer.parseInt(msg1);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                while ((msg2 = msgFromPlayer2.readLine()) != null) {
                    if (msg2.equals("end")) {
                        System.out.println("玩家2已死亡");
                        flag2 = true;
                        break;
                    } else {
                        score2 = Integer.parseInt(msg2);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        while (true){
            if(flag1 & flag2){
                sendmsg = score1+","+score2;
                this.sendmsg();
//                msgFromPlayer1.close();
//                msgFromPlayer2.close();
//                mList.get(0).close();
//                mList.get(1).close();
                break;
            }
//            System.out.println("还卡在这里");
        }
    }


    public void sendmsg() {
        System.out.println("发送信号");
        System.out.println(mList);
        for (int index = 0; index < 2; index++) {
            Socket mSocket = mList.get(index);
            PrintWriter pout = null;
            try {
                pout = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(mSocket.getOutputStream(), "UTF-8")), true);
                pout.println(sendmsg);//将输出流包装为打印流
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


