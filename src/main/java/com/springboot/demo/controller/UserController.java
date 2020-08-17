package com.springboot.demo.controller;

import com.springboot.demo.entity.*;
import com.springboot.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private Recent_readRepository recent_readRepository;
    @Autowired
    private User_groupRespository user_groupRespository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private CommentRepository commentRepository;

    public User getById(Integer id){
        if(id == null){
            return null;
        }
        System.out.println(userRepository);
        Optional<User>optional = userRepository.findById(25);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    @GetMapping("/users")
    public List<User> findAll(){
        return userRepository.findAll();
    }

    @PostMapping("/login")
    public Result login(@RequestBody User user){
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("phone", ExampleMatcher.GenericPropertyMatcher::exact)//模糊查询
                .withMatcher("password", ExampleMatcher.GenericPropertyMatcher::exact)
                .withIgnorePaths("id");
        Example <User> example = Example.of(user,matcher);
        List<User> matchUsers = userRepository.findAll(example);
        for(User matchUser :matchUsers){
            if(matchUser.getPhone().equals(user.getPhone()) &&
            matchUser.getPassword().equals(user.getPassword())){
                System.out.println(Result.success(matchUser).toString());
                return Result.success(matchUser);//成功的返回
            }
        }

        return Result.error(400,"账号或密码错误");//不成功的返回
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user){
        System.out.println(user.getWechat());
        if(user.getWechat().equals("")){user.setWechat(null);}
        if(user.getQq().equals("")){user.setQq(null);}
        if(user.getEmail().equals("")){user.setEmail(null);}

        for(User matchUser : findAll()){
            if(matchUser.getPhone() != null && matchUser.getPhone().equals(user.getPhone())){
                return Result.error(400,"手机号已被使用");
            }
            if(matchUser.getWechat() != null && matchUser.getWechat().equals(user.getWechat())){
                return Result.error(400,"微信已被使用");
            }
            if(matchUser.getQq() != null && matchUser.getQq().equals(user.getQq())){
                return Result.error(400,"QQ已被使用");
            }
            if(matchUser.getEmail() != null && matchUser.getEmail().equals(user.getEmail())){
                return Result.error(400,"邮箱已被使用");
            }
        }
        user.setId((int) (System.currentTimeMillis()%2000000011));
        System.out.println(user.toString());
        userRepository.save(user);
        return Result.success();
    }

    @GetMapping("/user/info")
    public Result userInfo(@RequestParam("id") int id){
        System.out.println("userInfo"+id);
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            return Result.success(optionalUser.get());
        }else{
            return Result.error(400,"用户不存在");
        }
    }

    @PostMapping("/user/save")
    public Result userSave(@RequestBody User user){
        userRepository.save(user);
        return Result.success();
    }

    @GetMapping("/user/own")
    public Result userOwn(@RequestParam("id") int id){
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            Document tmpDocu=new Document();
            tmpDocu.setCreator_id(id);
            tmpDocu.setIs_deleted(false);
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("creator_id", ExampleMatcher.GenericPropertyMatcher::exact)
                    .withIgnorePaths("id")
                    .withIgnorePaths("group_id")
                    .withIgnorePaths("create_time")
                    .withIgnorePaths("last_edit_time")
                    .withIgnorePaths("is_editting")
                    .withIgnorePaths("name");
            Example <Document> example = Example.of(tmpDocu,matcher);
            List<Document> myDocus=documentRepository.findAll(example);
            for(Document document : myDocus){
                superFuck(document,id);
            }
            return Result.success(myDocus);
        }else{
            return Result.error(400,"用户不存在");
        }
    }

    @GetMapping("/user/favorite")
    public Result userFavorite(@RequestParam("id") int id){
        List<Document> result = new ArrayList<>();
        for(Favorite favorite :favoriteRepository.findAll()){
            if (favorite.getFavorityKey().getUser_id() == id) {
                int doc_id = favorite.getFavorityKey().getDocument_id();
                Optional<Document> optional = documentRepository.findById(doc_id);
                if(optional.isPresent() && optional.get().isIs_deleted() == false){
                    result.add(superFuck(optional.get(),id));
                }
            }
        }
        System.out.println(result);
        return Result.success(result);
    }

    @GetMapping("/user/recent")
    public Result userRecent(@RequestParam("id") int id){
        System.out.println("recent "+id);
        Optional<Recent_read> optionalRecent_read = recent_readRepository.findById(id);
        if(optionalRecent_read.isPresent()){
            List<Document> result = new ArrayList<>();
            for(String document_id : optionalRecent_read.get().getDocument_list().split(",")){
                Optional<Document>optionalDocument = documentRepository.findById(Integer.parseInt(document_id));
                if(optionalDocument.isPresent() && optionalDocument.get().isIs_deleted() == false){
                    result.add(superFuck(optionalDocument.get(),id));
                }
            }
            System.out.println(result);
            return Result.success(result);
        }else{
            return Result.error(400,"用户不存在/没有访问记录");
        }
    }
    
    @GetMapping("/user/group")
    public Result userGroup( @RequestParam("id") Integer id){
        List<Group> list = new ArrayList<>();
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("creator_id",ExampleMatcher.GenericPropertyMatcher::exact)
                .withIgnorePaths("id").withIgnorePaths("name");
        User_group_relation user_group = new User_group_relation();
        user_group.setUser_group_relationKey(new User_group_relationKey(id,null));
        Example example = Example.of(user_group,matcher);
        for(Object object : user_groupRespository.findAll(example)){
            User_group_relation user_group_relation = (User_group_relation) object;
            int group_id = user_group_relation.getUser_group_relationKey().getGroup_id();
            Optional<Group>optional = groupRepository.findById(group_id);
            if(optional.isPresent()){
                list.add(optional.get());
            }
        }

        if(list.size() > 0){
            return Result.success(list);
        }else{
            return Result.error(400,"用户不存在或未参加团队");
        }
    }
    public Document fuck(Document document){
        User user = userRepository.findById(document.getCreator_id()).orElse(null);
        if(user != null){
            document.setUsername(user.getName());
        }
        return document;
    }
    public Document superFuck(Document document,int user_id){
        Integer doc_id=document.getId();
        Optional<Document> optionalDocument = documentRepository.findById(doc_id);
        Document tmpDoc=optionalDocument.get();
        //评论数
        Comment comment = new Comment();
        comment.setDocument_id(doc_id);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("doc_id",ExampleMatcher.GenericPropertyMatcher::exact)
                .withIgnorePaths("comment_id");
        Example<Comment>example = Example.of(comment,matcher);
        List<Comment> list = commentRepository.findAll(example);
        tmpDoc.setComments(list.size());
        //收藏数
        ExampleMatcher matchera = ExampleMatcher.matching()
                .withMatcher("document_id",ExampleMatcher.GenericPropertyMatcher::exact)
                .withIgnorePaths("user_id");
        Favorite favorite=new Favorite();
        favorite.setFavorityKey(new FavorityKey(null,doc_id));
        Example examplea = Example.of(favorite,matchera);
        List<Favorite> favoriteList=favoriteRepository.findAll(examplea);
        tmpDoc.setStars(favoriteList.size());
        document.setStar(favoriteRepository.findById(new FavorityKey(user_id,document.getId())).isPresent());
        return fuck(document);
    }
}
