
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qingzhengyu1111@outlook.com
 * @date ${DATE} ${TIME}
 */
public class OnlineSever {

    public static final int PORT = 6666;//端口号
    static List<Socket> mList = new ArrayList<>();
    private static ServerSocket server = null;

    public static void main(String[] args) {
        try {
            server = new ServerSocket(PORT);
            while (true) {
                // 阻塞并等待新连接
                Socket client = server.accept();
                System.out.println("已得到用户链接");
                mList.add(client);
                if(mList.size() == 2){
                    new Handler(mList).start();
                    //mList.removeAll(mList);
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // 别忘了处理异常
        }

    }

}




