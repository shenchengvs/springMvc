package holley.dubbo.open.impl;

import holley.dubbo.open.DemoService;
import holley.dubbo.open.model.User;

import java.util.ArrayList;
import java.util.List;

public class DemoServiceImpl implements DemoService {

    @Override
    public List<User> test() {
        User user = new User("1111111", 30);
        List l = new ArrayList();
        l.add(user);
        return l;
    }

}
