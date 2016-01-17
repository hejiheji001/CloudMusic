package com.fireawayh.cloudmusic.utils;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;

/**
 * Created by FireAwayH on 15/10/7.
 */
public class GUIUtils {

    private FileDialog savePath;
    private TextField saveText = new TextField();
    private TextField idText = new TextField();
    private Frame frame = new Frame("Cloud Music Downloader");
    private Dialog dialog = new Dialog(frame,"Hint", true);
    private List args = null;

    public GUIUtils(){
        super();
    }

    public void setArgs(List args){
        this.args = args;
    }

    public List getArgs(){
        return this.args;
    }

    private Point getCenter(Window f){
        int windowWidth = f.getWidth();                    //获得窗口宽
        int windowHeight = f.getHeight();
        Toolkit kit = Toolkit.getDefaultToolkit();             //定义工具包
        Dimension screenSize = kit.getScreenSize();            //获取屏幕的尺寸
        int screenWidth = screenSize.width;                    //获取屏幕的宽
        int screenHeight = screenSize.height;
        Point p = new Point(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);
        return p;
    }

    public void showGui(){
        savePath = new FileDialog(frame, "Save music to", FileDialog.SAVE);
        Label idLabel = new Label("Music id");
        Label saveLabel = new Label("Save path");
        idText.setColumns(15);
        idText.addKeyListener(
                new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if(e.getKeyCode() < 48 || e.getKeyCode() > 57){
                        }
                    }
                }
        );
        saveText.setColumns(15);
        saveText.setEditable(false);
        Button start = new Button("Start!");
        Button save = new Button("Save to...");
        frame.add(idLabel);
        frame.add(idText);
        frame.add(saveLabel);
        frame.add(saveText);
        frame.add(save);
        frame.add(start);
        frame.setLayout(new FlowLayout());
        frame.setSize(250,150);
        frame.setResizable(false);
        frame.setLocation(getCenter(frame));
        frame.setVisible(true);
        frame.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
            }
        );
        save.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        savePath.setFile("Please choose path");
                        savePath.setVisible(true);
                        String dirPath = savePath.getDirectory();
                        saveText.setText(dirPath);
                    }
                }
        );
        start.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        Label l = new Label();
                        l.setAlignment(Label.CENTER);
                        dialog.setSize(150, 50);
                        dialog.setLocation(getCenter(dialog));
                        dialog.addWindowListener(
                                new WindowAdapter() {
                                    @Override
                                    public void windowClosing(WindowEvent e) {
                                        dialog.removeAll();
                                        dialog.dispose();
                                    }
                                }
                        );

                        String songId = idText.getText();
                        String filePath = saveText.getText();

                        if(songId.isEmpty()){
                            l.setText("Music id required");
                            dialog.add(l);
                            dialog.setVisible(true);
                            return;
                        }

                        if(filePath.isEmpty()){
                            l.setText("Save path required");
                            dialog.add(l);
                            dialog.setVisible(true);
                            return;
                        }

                        DownloadUtils du = new DownloadUtils();
                        String result = du.downloadSongById(songId, filePath);
                        l.setText(result);
                        dialog.add(l);
                        dialog.setVisible(true);
                    }
                }
        );
    }

    public void printAscii(String filepath) {
        BufferedReader br = null;
        InputStream is = this.getClass().getResourceAsStream(filepath);
        String line = null;

        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    br = null;
                }
            }
        }
    }
}
