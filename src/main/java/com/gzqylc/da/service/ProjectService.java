package com.gzqylc.da.service;

import com.aliyuncs.exceptions.ClientException;
import com.gzqylc.da.dao.*;
import com.gzqylc.da.entity.*;
import com.gzqylc.da.web.RunnerHookController;
import com.gzqylc.da.web.logger.PipelineLogger;
import com.gzqylc.lang.web.JsonTool;
import com.gzqylc.lang.web.base.BaseService;
import com.gzqylc.lang.web.jpa.specification.Criteria;
import com.gzqylc.lang.web.jpa.specification.Restrictions;
import com.gzqylc.utils.HttpTool;
import lombok.Data;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Service
public class ProjectService extends BaseService<Project> {


    @Autowired
    RegistryDao registryDao;

    @Autowired
    HostDao hostDao;

    @Autowired
    RunnerDao runnerDao;

    @Autowired
    FrpService frpService;

    @Autowired
    RunnerService runnerService;

    @Autowired
    PipelineDao pipelineDao;

    @Autowired
    AppDao appDao;

    public void saveProject(Project project) {
        Registry registry = registryDao.findOne(project.getRegistry());

        project.setImageUrl(registry.getHost() + "/" + registry.getNamespace() + "/" + project.getName());
        if (project.getBuildConfig() == null) {
            App.BuildConfig buildConfig = new App.BuildConfig();


            project.setBuildConfig(buildConfig);
        }

        project = super.save(project);
    }


    public Pipeline.PipeProcessResult buildImage(String pipelineId, String pipeId, Pipeline.PipeBuildConfig cfg) throws GitAPIException, InterruptedException, IOException {
        PipelineLogger logger = PipelineLogger.getLogger(pipelineId);
        logger.info("开始构建镜像任务开始");

        Runner runner = runnerService.getRunner();
        Assert.state(runner != null, "执行节点为空，请先配置");
        logger.info("执行节点信息, {}", runner);

        BuildImageForm form = new BuildImageForm();
        form.gitUrl = cfg.getGitUrl();
        form.gitUsername = cfg.getGitUsername();
        form.gitPassword = cfg.getGitPassword();
        form.branch = cfg.getBranch();
        form.regHost = cfg.getRegistryHost();
        form.regUsername = cfg.getRegistryUsername();
        form.regPassword = cfg.getRegistryPassword();
        form.imageUrl = cfg.getImageUrl();
        form.buildContext = cfg.getContext();
        form.dockerfile = cfg.getDockerfile();
        form.logHook = cfg.getServerUrl() + RunnerHookController.API_LOG + "/" + pipelineId;
        form.resultHook = cfg.getServerUrl() + RunnerHookController.API_PIPE_FINISH + "/" + pipelineId + "/" + pipeId;


        form.gitUrl = form.gitUrl.replace(runner.getGitUrlReplaceSource(), runner.getGitUrlReplaceTarget());
        logger.info("替换git地址 {}", form.gitUrl);


        String frpServer = frpService.getFrpServer();
        int vhostHttpPort = frpService.getVhostHttpPort();

        String postData = JsonTool.toJsonQuietly(form);
        String postUrl = "http://" + frpServer + ":" + vhostHttpPort + "/agent/build";

        logger.info("请求地址 {}", postUrl);
        logger.info("请求数据 {}", postData);
        String resp = HttpTool.postJson(runner.getHost().getDockerId(), postUrl, postData);


        logger.info("响应数据 {}", resp);

        return Pipeline.PipeProcessResult.PROCESSING;


    }

    @Transactional
    public void deleteProject(String id) throws ClientException {
        Project project = baseDao.findOne(id);

        // 判断是否有应用依赖

        Criteria<App> c = new Criteria<>();
        c.add(Restrictions.eq(App.Fields.imageUrl, project.getImageUrl()));
        long count = appDao.count(c);

        Assert.state(count == 0, "项目[" + project.getName() + "]有关联应用，请先删除相关应用");


        // 删除构建日志
        List<Pipeline> list = pipelineDao.findAllByField("project.id", id);
        pipelineDao.deleteAll(list);

        // 删除镜像仓库
        IRepositoryService repositoryService = RepositoryServiceFactory.getRepositoryService(project.getRegistry());

        repositoryService.deleteTag(project.getImageUrl(), project.getRegistry());


        baseDao.deleteById(id);
    }


    @Data
    public static class BuildImageForm {
        String dockerfile;
        String gitUrl;
        String gitUsername;
        String gitPassword;
        String branch;
        String regHost;
        String regUsername;
        String regPassword;
        String imageUrl;
        String buildContext;
        String logHook;
        String resultHook;
    }
}
