package com.springboot.demo.controller;

import com.springboot.demo.entity.*;
import com.springboot.demo.repository.*;
import com.springboot.demo.tool.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.springboot.demo.entity.MessageType.*;

@CrossOrigin
@RestController
public class GroupController {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private User_groupRespository user_groupRespository;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private CommentRepository commentRepository;

    @PostMapping("/group/create")
    public Result create(@RequestBody Group group){
        group.setId((int) (System.currentTimeMillis()%2000000011));
        group.setDismissed(false);
        System.out.println(group.toString());
        groupRepository.save(group);

        User_group_relation user_group_relation = new User_group_relation(
                new User_group_relationKey(group.getCreator_id(),group.getId())
        );
        user_groupRespository.save(user_group_relation);
        return Result.success(group);
    }
    @GetMapping("/group/info")
    public Result info(@RequestParam("group_id")int group_id){
        Group group = groupRepository.findById(group_id).orElse(null);
        return Result.success(group);
    }

    @GetMapping("/group/member")
    public Result member(@RequestParam ("group_id") int group_id){
        List<User> list = new ArrayList<>();
        for(User_group_relation user_group_relation: user_groupRespository.findAll()){
            if(user_group_relation.getUser_group_relationKey().getGroup_id() == group_id){
                int user_id = user_group_relation.getUser_group_relationKey().getUser_id();
                Optional<User>optional = userRepository.findById(user_id);
                if(optional.isPresent()){
                    list.add(optional.get());
                }
            }
        }
        return Result.success(list);
    }

    @GetMapping("/group/document")
    public Result document(@RequestParam("group_id") int group_id,
                           @RequestParam("user_id") int user_id){
        List<Document> list = new ArrayList<>();
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("group_id",ExampleMatcher.GenericPropertyMatcher::exact)
                .withIgnorePaths("id").withIgnorePaths("creator_id").withIgnorePaths("create_time")
                .withIgnorePaths("last_edit_time").withIgnorePaths("is_editting")
                .withIgnorePaths("name").withIgnorePaths("edit_times").withIgnoreNullValues();
        Document document = new Document();
        document.setIs_deleted(false);
        document.setGroup_id(group_id);
        System.out.println(document);
        Example example = Example.of(document,matcher);
        list = documentRepository.findAll(example);
        for(Document items:list){
            Integer doc_id=items.getId();
            Optional<Document> optionalDocument = documentRepository.findById(doc_id);
            Document tmpDoc=optionalDocument.get();
            //评论数
            Comment comment = new Comment();
            comment.setDocument_id(doc_id);
            ExampleMatcher matcherb = ExampleMatcher.matching()
                    .withMatcher("doc_id",ExampleMatcher.GenericPropertyMatcher::exact)
                    .withIgnorePaths("comment_id");
            Example<Comment>exampleb = Example.of(comment,matcherb);
            List<Comment> lista = commentRepository.findAll(exampleb);
            tmpDoc.setComments(lista.size());
            //收藏数
            ExampleMatcher matchera = ExampleMatcher.matching()
                    .withMatcher("document_id",ExampleMatcher.GenericPropertyMatcher::exact)
                    .withIgnorePaths("user_id");
            Favorite favorite=new Favorite();
            favorite.setFavorityKey(new FavorityKey(null,doc_id));
            Example examplea = Example.of(favorite,matchera);
            List<Favorite> favoriteList=favoriteRepository.findAll(examplea);
            tmpDoc.setStars(favoriteList.size());
        }
        for(Document d : list){
            superFuck(d,user_id);
        }
        return Result.success(list);
    }

    @PostMapping("/group/invite")
    public Result invite(@RequestParam("user_id") int user_id,@RequestParam("group_id") int group_id){
        Optional<Group> optionalGroup = groupRepository.findById(group_id);
        Message message=new Message((int) (System.currentTimeMillis()%2000000011),
                user_id,optionalGroup.get().getCreator_id(),null,group_id, INVITE.ordinal(),false, Global.nowTime(),1);
        messageRepository.save(message);
        return Result.success();
    }

    @PostMapping("/group/apply")
    public Result apply(@RequestParam("user_id") int user_id,@RequestParam("group_id") int group_id){
        Optional<Group> optionalGroup = groupRepository.findById(group_id);
        Message message=new Message((int) (System.currentTimeMillis()%2000000011),
                optionalGroup.get().getCreator_id(),user_id,null,group_id, APPLY.ordinal(),false,Global.nowTime(),1);
        messageRepository.save(message);
        return Result.success();
    }

    @PostMapping("group/reply/invite")
    public Result replyinvite(@RequestParam("user_id") int user_id,@RequestParam("group_id") int group_id,
                              @RequestParam("msg_id")int msg_id,@RequestParam("yesno") boolean yesno){
        Optional<Group> optionalGroup = groupRepository.findById(group_id);
        Optional<Message> optionalMessage=messageRepository.findById(msg_id);
        Message tmpmsg=optionalMessage.get();
        if(yesno){
            tmpmsg.setOperate(2);
            tmpmsg.setHave_read(true);
            messageRepository.save(tmpmsg);
            Message message=new Message((int) (System.currentTimeMillis()%2000000011),
                    optionalGroup.get().getCreator_id(),user_id,null,group_id, AGREE_INVITE.ordinal(),false,Global.nowTime(),0);
            messageRepository.save(message);

            User_group_relationKey relationKey=new User_group_relationKey(user_id,group_id);
            User_group_relation relation=new User_group_relation(relationKey);
            user_groupRespository.save(relation);
        }
        else{
            tmpmsg.setOperate(3);
            tmpmsg.setHave_read(true);
            messageRepository.save(tmpmsg);
            Message message=new Message((int) (System.currentTimeMillis()%2000000011),
                    optionalGroup.get().getCreator_id(),user_id,null,group_id, REJECT_INVITE.ordinal(),false,Global.nowTime(),0);
            messageRepository.save(message);
        }
        return Result.success();
    }

    @PostMapping("group/reply/apply")
    public Result replyapply(@RequestParam("user_id") int user_id,@RequestParam("group_id") int group_id,
                             @RequestParam("msg_id")int msg_id,@RequestParam("yesno") boolean yesno){
        Optional<Group> optionalGroup = groupRepository.findById(group_id);
        Optional<Message> optionalMessage=messageRepository.findById(msg_id);
        Message tmpmsg=optionalMessage.get();
        if(yesno){
            tmpmsg.setOperate(2);
            tmpmsg.setHave_read(true);
            messageRepository.save(tmpmsg);
            Message message=new Message((int) (System.currentTimeMillis()%2000000011),
                    user_id,optionalGroup.get().getCreator_id(),null,group_id, AGREE_APPLY.ordinal(),false,Global.nowTime(),0);
            messageRepository.save(message);
            User_group_relationKey relationKey=new User_group_relationKey(user_id,group_id);
            User_group_relation relation=new User_group_relation(relationKey);
            user_groupRespository.save(relation);
        }
        else{
            tmpmsg.setOperate(3);
            tmpmsg.setHave_read(true);
            messageRepository.save(tmpmsg);
            Message message=new Message((int) (System.currentTimeMillis()%2000000011),
                    user_id,optionalGroup.get().getCreator_id(),null,group_id, REJECT_APPLY.ordinal(),false,Global.nowTime(),0);
            messageRepository.save(message);
        }
        return Result.success();
    }

    @PostMapping("/group/kickass")
    public Result kickass(@RequestParam("user_id") int user_id,@RequestParam("group_id") int group_id){
        Optional<Group> optionalGroup = groupRepository.findById(group_id);
        Message message=new Message((int) (System.currentTimeMillis()%2000000011),
                user_id,optionalGroup.get().getCreator_id(),null,group_id, KICK.ordinal(),false,Global.nowTime(),0);
        messageRepository.save(message);
        User_group_relationKey relationKey=new User_group_relationKey(user_id,group_id);
        User_group_relation relation=new User_group_relation(relationKey);
        user_groupRespository.delete(relation);
        return Result.success();
    }

    @PostMapping("/group/drop")
    public Result drop(@RequestParam("user_id") int user_id,@RequestParam("group_id") int group_id){
        Optional<Group> optionalGroup = groupRepository.findById(group_id);
        Message message=new Message((int) (System.currentTimeMillis()%2000000011),
                optionalGroup.get().getCreator_id(),user_id,null,group_id, DROP.ordinal(),false,Global.nowTime(),0);
        messageRepository.save(message);
        User_group_relationKey relationKey=new User_group_relationKey(user_id,group_id);
        User_group_relation relation=new User_group_relation(relationKey);
        user_groupRespository.delete(relation);
        return Result.success();
    }

    @PostMapping("/group/dismiss")
    public Result dismiss(@RequestParam("user_id") int user_id,@RequestParam("group_id") int group_id){
        Optional<Group> optionalGroup = groupRepository.findById(group_id);
        Group aimgroup=optionalGroup.get();
        if(!aimgroup.getCreator_id().equals(user_id)){
            return Result.error(400,"用户无解散权限");
        }
        aimgroup.setDismissed(true);
        List<User_group_relation> relations=user_groupRespository.findAll();
        for(User_group_relation relation:relations){
            if(relation.getUser_group_relationKey().getGroup_id().equals(group_id)){
                user_groupRespository.delete(relation);
                Message message=new Message((int) (System.currentTimeMillis()%2000000011),
                        relation.getUser_group_relationKey().getUser_id(),user_id,null,group_id, DISMISS_INFORM.ordinal(),false,Global.nowTime(),0);
                messageRepository.save(message);
            }
        }
        groupRepository.save(aimgroup);
        return Result.success();
    }
    public Document fuck(Document document){
        User user = userRepository.findById(document.getCreator_id()).orElse(null);
        if(user != null){
            document.setUsername(user.getName());
        }
        return document;
    }
    public Document superFuck(Document document,int user_id){
        document.setStar(favoriteRepository.findById(new FavorityKey(user_id,document.getId())).isPresent());
        return fuck(document);
    }
}
