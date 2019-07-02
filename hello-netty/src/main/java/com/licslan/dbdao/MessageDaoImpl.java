package com.licslan.dbdao;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * @author LICSLAN
 * */
@Service
public class MessageDaoImpl implements MessageDao{
    @Override
    public List<Hobby> getList() {
        List<Hobby> hobbyList = new ArrayList<>();
        Hobby hobby;
        Random random;
        for(int i=0;i<10;i++){
            hobby= new Hobby();
            random = new Random();
            hobby.setName(String.valueOf(random.nextInt(10)));
            hobby.setAge(random.nextInt(10));
            hobby.setTime((long) random.nextInt(10));
            hobby.setType(String.valueOf(random.nextInt(10)));
            hobbyList.add(hobby);
        }
        return hobbyList;
    }

    @Override
    public Hobby getHobby() {
        //TODO
        return null;
    }
}
@Data
class Hobby{
    private String name;
    private String type;
    private int age;
    private Long time;

}
