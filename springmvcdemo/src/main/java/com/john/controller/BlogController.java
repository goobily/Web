package com.john.controller;

import com.john.model.BlogEntity;
import com.john.model.UserEntity;
import com.john.repository.BlogRepo;
import com.john.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by john_wu on 2017/5/5.
 */
@Controller
public class BlogController {
    @Autowired
    BlogRepo blogRepository;

    @Autowired
    UserRepo userRepository;

    // 查看所有博文
    @RequestMapping(value = "/admin/blogs", method = RequestMethod.GET)
    public String showBlogs(ModelMap modelMap) {
        List<BlogEntity> blogList = blogRepository.findAll();
        modelMap.addAttribute("blogList", blogList);
        return "admin/blogs";
    }

    // 添加博文
    @RequestMapping(value = "admin/blogs/add", method = RequestMethod.GET)
    public String addBlog(ModelMap modelMap) {
        List<UserEntity> userList = userRepository.findAll();
        //
        modelMap.addAttribute("userList", userList);
        return "admin/addBlog";
    }

//    // 添加博文，POST请求，重定向为查看博客页面
//    @RequestMapping(value = "/admin/blogs/addP", method = RequestMethod.POST)
//    public String addBlogPost(@ModelAttribute("blog") BlogEntity blogEntity) {
//        // 打印博客标题
//        System.out.println(blogEntity.getTitle());
//        // 打印博客作者
//        System.out.println(blogEntity.getUser().getNickname());
//        // 存库
//        blogRepository.saveAndFlush(blogEntity);
//        // 重定向地址
//        return "redirect:/admin/blogs";
//    }
    // 添加博文，POST请求，重定向为查看博客页面
    @RequestMapping(value = "/admin/blogs/addP", method = RequestMethod.POST)
    public String addBlogPost(@RequestParam("title") String title, @RequestParam("user.id") Integer userId, @RequestParam("content") String content,
                              @RequestParam("pubDate") String pubDate) throws ParseException {
        BlogEntity blogEntity = new BlogEntity();

        blogEntity.setContent(content);

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        blogEntity.setPubDate(format.parse(pubDate));
        blogEntity.setTitle(title);
        blogEntity.setUser(userRepository.findOne(userId));

        blogRepository.saveAndFlush(blogEntity);
        // 重定向地址
        return "redirect:/admin/blogs";
    }

    // 查看博文详情，默认使用GET方法时，method可以缺省
    @RequestMapping(value = "/admin/blogs/show/{id}", method = RequestMethod.GET)
    public String showBlog(@PathVariable("id") int id, ModelMap modelMap) {
        BlogEntity blog = blogRepository.findOne(id);
        modelMap.addAttribute("blog", blog);
        return "admin/blogDetail";
    }

    // 修改博文内容，页面
    @RequestMapping(value = "/admin/blogs/update/{id}", method = RequestMethod.GET)
    public String updateBlog(@PathVariable("id") int id, ModelMap modelMap) {
        BlogEntity blog = blogRepository.findOne(id);
        List<UserEntity> userList = userRepository.findAll();
        modelMap.addAttribute("blog", blog);
        modelMap.addAttribute("userList", userList);
        return "admin/updateBlog";
    }

//    // 修改内容，POST请求
//    @RequestMapping(value = "/admin/blogs/updateP", method = RequestMethod.POST)
//    public String updateBlogP(@ModelAttribute("blogP") BlogEntity blogEntity) {
//        // update
//        blogRepository.updateBlog(blogEntity.getTitle(), blogEntity.getUser().getId(), blogEntity.getContent(), blogEntity.getPubDate(), blogEntity.getId());
//        blogRepository.flush();
//        return "redirect:/admin/blogs";
//    }

    // 修改内容，POST请求
    @RequestMapping(value = "/admin/blogs/updateP", method = RequestMethod.POST)
    public String updateBlogP(@RequestParam("title") String title, @RequestParam("user.id") Integer userId, @RequestParam("content") String content,
                              @RequestParam("pubDate") String pubDate, @RequestParam("id") Integer id) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        blogRepository.updateBlog(title, userId, content, format.parse(pubDate), id);
        blogRepository.flush();
        return "redirect:/admin/blogs";
    }

    // 删除博文
    @RequestMapping(value = "/admin/blogs/delete/{id}", method = RequestMethod.GET)
    public String deleteBlog(@PathVariable("id") int id) {
        blogRepository.delete(id);
        blogRepository.flush();
        return "redirect:/admin/blogs";
    }


}
