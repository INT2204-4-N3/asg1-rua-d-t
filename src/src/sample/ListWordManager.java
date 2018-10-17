package sample;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ListWordManager {
    private   Map <String, String> List1= new HashMap<String, String>();

    public Map<String, String> getList1() {
        return List1;
    }
    public void getpart1() {


    }
    public void CreatListWord(String Name_File) throws Exception {
        FileReader fr = new FileReader(Name_File);
        BufferedReader br= new BufferedReader(fr);
        String i;
        while ((i = br.readLine()) != null) {
            String part1=i.substring(0, i.indexOf("<")/*tim vi tri*/);
            String part2=i.substring(i.indexOf("<"), i.length());
            List1.put(part1,part2);
        }
        br.close();
        fr.close();
    }
    public void AddWord(){
        /*this.setVisible(false);
        int check;
        String word = tfNewWord.getText();
        String read = tfNewRead.getText();
        MyWord mWord = new MyWord(word,read);
        System.out.println(mWord);
        check = ult.addWord(mWord, DictGui.rf.tm);
        System.out.println(DictGui.rf.tm.keySet());
        if(check==ult.exsit)
            JOptionPane.showMessageDialog(null, "Từ đã tồn tại");
        else if(check == ult.notFull)
            JOptionPane.showMessageDialog(null, "Bạn chưa điền hết vào chỗ trống");
        else
        {
            JOptionPane.showMessageDialog(null, "Thêm từ thành công");
            try {
                wtf = new WriteToFile("loanword");
                wtf.Write(DictGui.rf.tm.values());
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DialogAdd.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(DialogAdd.class.getName()).log(Level.SEVERE, null, ex);
            }*/




    }
  /*  public static void main(String[] argvs) throws Exception {
        ListWordManager t= new ListWordManager();
        t.CreatListWord("C:\\BT lon UET diction\\uet-dictionary\\src\\Sourse\\rua_dat.txt");
        Set<String> set = t.List1.keySet();
        for (String key : set) {
            System.out.println(key + " " + t.List1.get(key));
        }
    }*/
}
