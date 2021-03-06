package com.gzqylc.docker.admin.web;

import com.gzqylc.docker.admin.entity.Registry;
import com.gzqylc.docker.admin.entity.Repository;
import com.gzqylc.docker.admin.service.IRepositoryService;
import com.gzqylc.docker.admin.service.RegistryService;
import com.gzqylc.docker.admin.service.RepositoryServiceFactory;
import com.gzqylc.framework.Route;
import com.gzqylc.lang.bean.Option;
import com.gzqylc.lang.web.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Route("api/repository")
@RestController
public class RepositoryController extends BaseController {

    @Autowired
    RegistryService registryService;


    @Route("list")
    public Page<Repository> list(Pageable pageable, String keyword, @RequestParam(required = true) String registryId) throws Exception {
        Registry registry = registryService.findOne(registryId);

        IRepositoryService repositoryService = RepositoryServiceFactory.getRepositoryService(registry);

        Page<Repository> list = repositoryService.findRepositoryList(pageable, registry, keyword);

        return list;
    }

    @Route("tagList")
    public List<Option> options(@RequestParam String url, String[] selected) throws Exception {
        IRepositoryService repositoryService = RepositoryServiceFactory.getRepositoryService(url);


        Registry registry = registryService.findByUrl(url);

        List<String> list = repositoryService.findTagList(url, registry);

        return super.convert(list, i -> new Option(i, i));

    }
}
