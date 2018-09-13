package holley.dubbo.open.impl;

import holley.dubbo.open.DemoService;
import holley.dubbo.open.model.User;

import java.util.ArrayList;
import java.util.List;

public class DemoServiceImpl2 implements DemoService {

    @Override
    public List<User> test() {
        User user = new User("2222222", 30);
        List l = new ArrayList();
        l.add(user);
        return l;
    }

}
