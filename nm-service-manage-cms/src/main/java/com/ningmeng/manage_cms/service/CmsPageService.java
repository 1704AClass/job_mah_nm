package com.ningmeng.manage_cms.service;

import com.ningmeng.framework.domain.cms.CmsPage;
import com.ningmeng.framework.domain.cms.request.QueryPageRequest;
import com.ningmeng.framework.domain.cms.response.CmsCode;
import com.ningmeng.framework.domain.cms.response.CmsPageResult;
import com.ningmeng.framework.exception.CustomException;
import com.ningmeng.framework.exception.CustomExceptionCast;
import com.ningmeng.framework.model.response.CommonCode;
import com.ningmeng.framework.model.response.QueryResponseResult;
import com.ningmeng.framework.model.response.QueryResult;
import com.ningmeng.framework.model.response.ResponseResult;
import com.ningmeng.manage_cms.dao.CmsPageRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CmsPageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    //添加
    public ResponseResult add(CmsPage cmsPage){
        if (cmsPage == null){
            //向外抛出 页面对象为空异常
            CustomExceptionCast.cast(CommonCode.FAIL);
        }
        //添加之前 我们要查询 站点id 和页面名称以及web路径
        List<CmsPage> list = cmsPageRepository.findBySiteIdAndPageNameAndPageWebPath(cmsPage.getSiteId(),cmsPage.getPageName(),cmsPage.getPageWebPath());
        //不存在 添加
        if (list != null){
            CustomExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        //不存在 才添加
        cmsPage.setPageCreateTime(new Date());
        cmsPageRepository.save(cmsPage);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        if (queryPageRequest == null) {
            queryPageRequest = new QueryPageRequest();
        }
        if (page <= 0){
            page =1;
        }
        page = page-1;
        if (size <=0){
            size =20;
        }
        PageRequest pageRequest =PageRequest.of(page,size);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        //条件值         
        CmsPage cmsPage = new CmsPage();
        //站点ID         
        if(StringUtils.isNotEmpty(queryPageRequest.getSiteId())){
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        //页面别名  
        if(StringUtils.isNotEmpty(queryPageRequest.getPageAliase())){
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        //创建条件实例     
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);
        Page<CmsPage> all = cmsPageRepository.findAll(example,pageRequest);
        QueryResult<CmsPage> cmsPageQueryResult = new QueryResult<CmsPage>();
        cmsPageQueryResult.setList(all.getContent());
        cmsPageQueryResult.setTotal(all.getTotalElements());
        //返回结果
        return  new QueryResponseResult(CommonCode.SUCCESS,cmsPageQueryResult);

    }

    /**
     * 回显
     * @param id
     * @return
     */
    public CmsPage getById(String id) {
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        if (optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    /**
     * 修改方法
     * @param cmsPage
     * @return
     */
    public CmsPageResult update(CmsPage cmsPage) {
        //先查询对象是否存在
       CmsPage cmsPage1 = this.getById(cmsPage.getPageId());
       if (cmsPage1 != null){
           cmsPageRepository.save(cmsPage);
           return new CmsPageResult(CommonCode.SUCCESS,null);
       }
        return new CmsPageResult(CommonCode.FAIL,null);
    }

    /**
     * 删除
     */
    public ResponseResult delete(String id){
        CmsPage cmsPage = this.getById(id);
        if (cmsPage != null){
            cmsPageRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }
}
