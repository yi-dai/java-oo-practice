package com.twu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.List;

public class Main {
    public static Scanner sca = new Scanner(System.in);
    public static List<Hot_search> hot_search = new ArrayList<>();

    public static void main(String[] args) {
        while(true) {
            System.out.println("欢迎来到热搜排行榜，你是？");
            System.out.println("1.用户");
            System.out.println("2.管理员");
            System.out.println("3.退出");
            
            int user_type = sca.nextInt();
            if (user_type == 3) {break;}
            
            switch(user_type){
                case 1 :{
                    System.out.println("请输入您的昵称：");
                    User u = new User();
                    String u_name = sca.next();
                    while(true){
                        String a = "你好， " + u_name + "， 你可以：";
                        System.out.println(a);
                        System.out.println("1.查看热搜排行榜");
                        System.out.println("2.给热搜事件投票");
                        System.out.println("3.购买热搜");
                        System.out.println("4.添加热搜");
                        System.out.println("5.退出");
                        int u_transaction = sca.nextInt();
                        if (u_transaction == 5) {break;}
                        switch(u_transaction){
                            case 1 : u.view_hot_search_ranking_list(); break;
                            case 2 : u.vote_hot_search(); break;
                            case 3 : u.purchase_hot_search(); break;
                            case 4 : u.add_hot_search(); break;
                            default: break;
                        }
                    }
                    continue;
                }
                case 2 : {
                    Host h = new Host();
                    List<String> host_list = h.get_host_list();
                    String h_name;
                    while(true){
                        System.out.println("请输入您的昵称：");
                        h_name = sca.next();
                        System.out.println("请输入你的密码：");
                        String h_password = sca.next();
                        String t = h_name + " " + h_password;
                        if (host_list.contains(t)){
                            break;
                        } else {
                            System.out.println("您输入的昵称或密码不正确，请重新输入。");
                            continue;
                        }
                    }
                    while(true){
                        System.out.println("你好， " + h_name + "， 你可以：");
                        System.out.println("1.查看热搜排行榜");
                        System.out.println("2.添加热搜");
                        System.out.println("3.添加超级热搜");
                        System.out.println("4.退出");
                        int h_transaction = sca.nextInt();
                        if (h_transaction == 4) {break;}
                        switch(h_transaction){
                            case 1 : h.view_hot_search_ranking_list();break;
                            case 2 : h.add_hot_search();break;
                            case 3 : h.add_super_hot_search();break;
                            default: break;
                        }
                    }
                    continue;
                }
                default : {
                    sca.close();
                    break;
                }
            }
        }  
    }
}

class People {
    public void view_hot_search_ranking_list(){
        Collections.sort(Main.hot_search, new Comparator<Hot_search>() {
            @Override
            public int compare(Hot_search o1, Hot_search o2){
                if (o1.get_price() > 0 || o2.get_price()>0){
                    return 0;
                }else{
                    if (o1.get_vote_num() < o2.get_vote_num()){
                        return 1;
                    } else if (o1.get_vote_num() == o2.get_vote_num()){
                        return 0;
                    } else {
                        return -1;
                    }
                }
            }
        }); 
        int i = 1;
        for (Hot_search hs : Main.hot_search) {
            System.out.println(i + " " + hs.get_hot_search() + " " + hs.get_vote_num());
            i += 1;
        }
    }
    public void add_hot_search(){
        while (true){
            System.out.println("请输入你要添加的热搜事件的名字：");

            String new_hot = Main.sca.next();
            List<String> hs_list = Main.hot_search.stream().map(a -> a.get_hot_search()).collect(Collectors.toList());
            if (hs_list.contains(new_hot)) {
                System.out.println("您输入的热搜已存在，请重新输入：");
            } else {
                Hot_search hs = new Hot_search(new_hot,0);
                Main.hot_search.add(hs);
                System.out.println("添加成功");
                break;
            }
        }
    }
}

class User extends People {
    int vote_number;
    User(){
        this.vote_number = 10;
    }
    public void purchase_hot_search() {
        System.out.println("请输入你要购买的热搜名称：");
        String p_hs = Main.sca.next();
        System.out.println("请输入你要购买的热搜排名：");
        int p_position = Main.sca.nextInt();
        System.out.println("请输入你要购买的热搜金额：");
        int p_price = Main.sca.nextInt();
        List<String> hs_list = Main.hot_search.stream().map(a -> a.get_hot_search()).collect(Collectors.toList());
        int index_of_hs = hs_list.indexOf(p_hs);
        Hot_search new_hs = Main.hot_search.get(index_of_hs);
        new_hs.set_price(p_price);
        Hot_search old_hs = Main.hot_search.get(p_position - 1);
        if (old_hs.get_price() == 0){
            Main.hot_search.remove(new_hs);
            Main.hot_search.add(p_position - 1, new_hs);
        } else{
            if (old_hs.get_price() >= p_price){
                return;
            }
            Main.hot_search.remove(new_hs);
            Main.hot_search.set(p_position - 1, new_hs);
        }
    }
    public void vote_hot_search(){
        String v_hs;
        while(true){
            System.out.println("请输入你要投票的热搜名称：");
            v_hs = Main.sca.next();
            List<String> hs_list = Main.hot_search.stream().map(a -> a.get_hot_search()).collect(Collectors.toList());
            if (hs_list.contains(v_hs)) {
                break;
            } else {
                System.out.println("您输入的热搜不存在，请投票给已存在的热搜。");
                continue;
            }
        }
        System.out.println("请输入你要投票的热搜票数：（你目前还有" + vote_number + "票）");
        int v_nm = Main.sca.nextInt();
        if (v_nm > vote_number){
            System.out.println("票数不足，投票失败");
            return;
        }
        Iterator it = Main.hot_search.iterator();
        while (it.hasNext()){
            Hot_search ohs = (Hot_search) it.next();
            int new_num ;
            if (ohs.get_hot_search().equals(v_hs)){
                if (ohs.get_sup()) {
                    new_num= ohs.get_vote_num() + v_nm * 2;
                } else{
                    new_num = ohs.get_vote_num() + v_nm;
                }
                ohs.set_vote_num(new_num);
                vote_number -= v_nm; 
            }
        }
    }
}

class Host extends People{
    public List<String> get_host_list(){
        List<String> host_list = new ArrayList<String>();
        host_list.add("host1 abc");
        host_list.add("host2 def");
        host_list.add("host3 ghk");
        return host_list;
    } 
    public void add_super_hot_search(){
        String v_hs;
        System.out.println("请输入你要添加的超级热搜事件的名字：");
        v_hs = Main.sca.next();
        List<String> hs_list = Main.hot_search.stream().map(a -> a.get_hot_search()).collect(Collectors.toList());
        if (hs_list.contains(v_hs)) {
            Iterator it = Main.hot_search.iterator();
            while (it.hasNext()){
                Hot_search ohs = (Hot_search) it.next();
                if (ohs.get_hot_search().equals(v_hs)){
                    ohs.set_sup(true);
                }
            }
        } else {
            Hot_search hs = new Hot_search(v_hs,0);
            hs.set_sup(true);
            Main.hot_search.add(hs);
        }
        System.out.println("已添加超级热搜");
    }
}

class Hot_search{
    private String hot_search;
    private int vote_num;
    private int price;
    private boolean sup;
    Hot_search(){super();}
    Hot_search(String hot_search,int vote_num){
        super();
        this.hot_search = hot_search;
        this.vote_num = vote_num;
        this.price = 0;
        this.sup = false;
    }
    public void set_vote_num(int vm){
        this.vote_num = vm;
    }
    public String get_hot_search(){
        return hot_search;
    }

    public void set_sup(boolean s){
        this.sup = s;
    }
    public boolean get_sup(){
        return sup;
    }

    public int get_vote_num(){
        return vote_num;
    }
    public int get_price(){
        return price;
    }
    public void set_price(int p){
        this.price = p;
    }
}