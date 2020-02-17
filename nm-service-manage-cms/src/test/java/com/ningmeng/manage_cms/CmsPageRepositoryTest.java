package com.ningmeng.manage_cms;

import com.ningmeng.framework.domain.cms.CmsPage;
import com.ningmeng.framework.domain.cms.CmsPageParam;
import com.ningmeng.manage_cms.dao.CmsPageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest{
    @Autowired
    CmsPageRepository cmsPageRepository;

    //查询
    @Test
    public  void findAll(){
        List<CmsPage> list = cmsPageRepository.findAll();
        for (CmsPage cmsPage:list
             ) {
            System.out.println("hahhahhahahahha");
            System.out.println(cmsPage.getPageName());
        }
    }
    //分页
    @Test
    public void testFindPage() {
        int page = 0;
        //从0开始    
        int size = 10;
        //每页记录数
        Pageable pageable = PageRequest.of(page,size);
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        System.out.println(all);
    }
    //添加
    @Test
    public void testInsert(){
        //定义实体类
//        CmsPage cmsPage = new CmsPage();
//        cmsPage.setSiteId("s01");
//        cmsPage.setTemplateId("t01");
//        cmsPage.setPageName("测试页面");
//        cmsPage.setPageCreateTime(new Date());
//        List<CmsPageParam> cmsPageParams = new ArrayList<>();
//        CmsPageParam cmsPageParam = new CmsPageParam();
//        cmsPageParam.setPageParamName("param1");
//        cmsPageParam.setPageParamValue("value1");
//        cmsPageParams.add(cmsPageParam);
//        cmsPage.setPageParams(cmsPageParams);
//        cmsPageRepository.save(cmsPage);
//        System.out.println(cmsPage);
    }

    //删除
    @Test
    public void testDelete() {
        cmsPageRepository.deleteById("5abefd525b05aa293098fca6");
    }

    //修改
    @Test
    public void testUpdate() {
        //jdk1.8 引入新的对象optional
        Optional<CmsPage> optional = cmsPageRepository.findById("5b17a34211fe5e2ee8c116c9");
        //非空判断 true 不为空 false 为空
        if(optional.isPresent()){
            CmsPage cmsPage = optional.get();
            cmsPage.setPageName("测试页面");
            //修改 添加 使用的 都是save方法  判断id
            cmsPageRepository.save(cmsPage);
        }else{
            System.out.println("修改失败");
        }
    }

    @Test
    public  void  testFindByName(){
        //根据名称查询
        //CmsPage cmsPage = cmsPageRepository.findByPageName("测试方法单");
        //根据名称和类型 查询
        //CmsPage cmsPage = cmsPageRepository.findByPageNameAndPageType("测试方法双","1");


        //System.out.println(cmsPage);
    }



    @Test
    public void testFindAll() {
        //条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
                exampleMatcher = exampleMatcher.withMatcher("pageAliase",
                        ExampleMatcher.GenericPropertyMatchers.contains());
                //页面别名模糊查询，需要自定义字符串的匹配器实现模糊查询   
                //ExampleMatcher.GenericPropertyMatchers.contains() 包含
                // ExampleMatcher.GenericPropertyMatchers.startsWith()//开头匹配
               //条件值       
                CmsPage cmsPage = new CmsPage();
                //站点ID         
                cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");
                //模板ID
                cmsPage.setTemplateId("5a962c16b00ffc514038fafd");
                //        cmsPage.setPageAliase("分类导航");         
        // 创建条件实例
                Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);

                Pageable pageable = new PageRequest(0, 10);
                Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
                System.out.println(all);


    }
}
