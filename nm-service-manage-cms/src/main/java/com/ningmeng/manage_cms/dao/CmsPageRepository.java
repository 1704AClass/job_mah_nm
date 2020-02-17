package com.ningmeng.manage_cms.dao;

import com.ningmeng.framework.domain.cms.CmsPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CmsPageRepository extends MongoRepository<CmsPage,String> {
    //自定义命名查询
    public Page<CmsPage> findByPageName(String pageName,Pageable pageable);
    public CmsPage findByPageNameAndPageType(String pageName,String pageType);

    int countBySiteIdAndPageType(String siteid,String pageType);

    List<CmsPage> findBySiteIdAndPageNameAndPageWebPath(String SiteId, String PageName, String PageWebPath);
    //根据站点和页面类型分页查询    
   // Page<CmsPage> findBySiteIdAndPageType(String siteId, String pageType, Pageable pageable);
}
