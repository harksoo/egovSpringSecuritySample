package egovframework.com.uss.olp.qqm.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springmodules.validation.commons.DefaultBeanValidator;

import egovframework.com.cmm.ComDefaultCodeVO;
import egovframework.com.cmm.ComDefaultVO;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.annotation.IncludedInfo;
import egovframework.com.cmm.service.EgovCmmUseService;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.uss.olp.qqm.service.EgovQustnrQestnManageService;
import egovframework.com.uss.olp.qqm.service.QustnrQestnManageVO;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.bind.annotation.CommandMap;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
/**
 * 설문문항을 처리하는 Controller Class 구현
 * @author 공통서비스 장동한
 * @since 2009.03.20
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.03.20  장동한          최초 생성
 *   2011.8.26	정진오			IncludedInfo annotation 추가
 *
 * </pre>
 */ 

@Controller
public class EgovQustnrQestnManageController {
	
	 
	 
	
	protected Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	private DefaultBeanValidator beanValidator;
	
	/** EgovMessageSource */
    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;
	
	@Resource(name = "egovQustnrQestnManageService")
	private EgovQustnrQestnManageService egovQustnrQestnManageService;
	 
    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;
    
	@Resource(name="EgovCmmUseService")
	private EgovCmmUseService cmmUseService;
    
    /**
     * 설문항목 통계를 조회한다. 
     * @param searchVO
     * @param qustnrQestnManageVO
     * @param commandMap
     * @param model
     * @return "egovframework/com/uss/olp/qqm/EgovQustnrQestnManageStatistics"
     * @throws Exception
     */
    @RequestMapping(value="/uss/olp/qqm/EgovQustnrQestnManageStatistics.do")
	public String egovQustnrQestnManageStatistics(
			@ModelAttribute("searchVO") ComDefaultVO searchVO, 
			QustnrQestnManageVO qustnrQestnManageVO,
			@CommandMap Map commandMap,
    		ModelMap model)
    throws Exception {

		String sLocationUrl = "egovframework/com/uss/olp/qqm/EgovQustnrQestnManageStatistics";
		
        List sampleList = egovQustnrQestnManageService.selectQustnrQestnManageDetail(qustnrQestnManageVO);
        model.addAttribute("resultList", sampleList);
        // 객관식설문통계
        HashMap mapParam = new HashMap();
        mapParam.put("qestnrQesitmId", (String)qustnrQestnManageVO.getQestnrQesitmId());
        List statisticsList = egovQustnrQestnManageService.selectQustnrManageStatistics(mapParam);
        model.addAttribute("statisticsList", statisticsList);
        // 주관식설문통계
        List statisticsList2 = egovQustnrQestnManageService.selectQustnrManageStatistics2(mapParam);
        model.addAttribute("statisticsList2", statisticsList2);
		return sLocationUrl; 	
	}
	
	/**
	 * 설문문항 팝업 록을 조회한다. 
	 * @param searchVO
	 * @param qustnrQestnManageVO
	 * @param commandMap
	 * @param model
	 * @return "egovframework/com/uss/olp/qqm/EgovQustnrQestnManageListPopup"
	 * @throws Exception
	 */
	@RequestMapping(value="/uss/olp/qqm/EgovQustnrQestnManageListPopup.do")
	public String egovQustnrQestnManageListPopup(
			@ModelAttribute("searchVO") ComDefaultVO searchVO, 
			@ModelAttribute("qustnrQestnManageVO") QustnrQestnManageVO qustnrQestnManageVO,
			@CommandMap Map commandMap, 
    		ModelMap model)
    throws Exception {
		
		String sSearchMode = commandMap.get("searchMode") == null ? "" : (String)commandMap.get("searchMode");

		//설문지정보에서 넘어오면 자동검색 설정
		if(sSearchMode.equals("Y")){
			searchVO.setSearchCondition("QESTNR_ID");
			searchVO.setSearchKeyword(qustnrQestnManageVO.getQestnrId());
		}

    	/** EgovPropertyService.sample */
    	searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
    	searchVO.setPageSize(propertiesService.getInt("pageSize"));

    	/** pageing */
    	PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
		paginationInfo.setPageSize(searchVO.getPageSize());
		
		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
        List resultList = egovQustnrQestnManageService.selectQustnrQestnManageList(searchVO);
        model.addAttribute("resultList", resultList);
                
        int totCnt = (Integer)egovQustnrQestnManageService.selectQustnrQestnManageListCnt(searchVO);
		paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);
		
		return "egovframework/com/uss/olp/qqm/EgovQustnrQestnManageListPopup"; 
	}
	
	/**
	 * 설문문항 목록을 조회한다. 
	 * @param searchVO
	 * @param qustnrQestnManageVO
	 * @param commandMap
	 * @param model
	 * @return "egovframework/com/uss/olp/qqm/EgovQustnrQestnManageList"
	 * @throws Exception
	 */
	@IncludedInfo(name="질문관리", order = 630 ,gid = 50)
	@RequestMapping(value="/uss/olp/qqm/EgovQustnrQestnManageList.do")
	public String egovQustnrQestnManageList(
			@ModelAttribute("searchVO") ComDefaultVO searchVO, 
			@ModelAttribute("qustnrQestnManageVO") QustnrQestnManageVO qustnrQestnManageVO,
			@CommandMap Map commandMap, 
    		ModelMap model)
    throws Exception {
    	// 0. Spring Security 사용자권한 처리
    	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
    	if(!isAuthenticated) {
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.login"));
        	return "egovframework/com/uat/uia/EgovLoginUsr";
    	}
    	
		//로그인 객체 선언
		LoginVO loginVO = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		
		String sCmd = commandMap.get("cmd") == null ? "" : (String)commandMap.get("cmd");
		String sSearchMode = commandMap.get("searchMode") == null ? "" : (String)commandMap.get("searchMode");
		
		if(sCmd.equals("del")){
			egovQustnrQestnManageService.deleteQustnrQestnManage(qustnrQestnManageVO);
		}
		
		//설문지정보에서 넘어오면 자동검색 설정
		if(sSearchMode.equals("Y")){
			searchVO.setSearchCondition("QESTNR_ID");
			searchVO.setSearchKeyword(qustnrQestnManageVO.getQestnrId());
		}

    	/** EgovPropertyService.sample */
    	searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
    	searchVO.setPageSize(propertiesService.getInt("pageSize"));
    	
    	/** pageing */
    	PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
		paginationInfo.setPageSize(searchVO.getPageSize());
		
		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
        List sampleList = egovQustnrQestnManageService.selectQustnrQestnManageList(searchVO);
        model.addAttribute("resultList", sampleList);
                
        int totCnt = (Integer)egovQustnrQestnManageService.selectQustnrQestnManageListCnt(searchVO);
		paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);
        
		return "egovframework/com/uss/olp/qqm/EgovQustnrQestnManageList"; 
	}
	
	/**
	 * 설문문항 목록을 상세조회 조회한다. 
	 * @param searchVO
	 * @param qustnrQestnManageVO
	 * @param commandMap
	 * @param model
	 * @return "egovframework/com/uss/olp/qqm/EgovQustnrQestnManageDetail"
	 * @throws Exception
	 */
	@RequestMapping(value="/uss/olp/qqm/EgovQustnrQestnManageDetail.do")
	public String egovQustnrQestnManageDetail(
			@ModelAttribute("searchVO") ComDefaultVO searchVO, 
			@ModelAttribute("qustnrQestnManageVO") QustnrQestnManageVO qustnrQestnManageVO,
			@CommandMap Map commandMap,
    		ModelMap model)
    throws Exception {
		
		String sLocationUrl = "egovframework/com/uss/olp/qqm/EgovQustnrQestnManageDetail";
		
		String sCmd = commandMap.get("cmd") == null ? "" : (String)commandMap.get("cmd");
		
		if(sCmd.equals("del")){
			egovQustnrQestnManageService.deleteQustnrQestnManage(qustnrQestnManageVO);
			/** 목록으로갈때 검색조건 유지 */
			sLocationUrl = "redirect:/uss/olp/qqm/EgovQustnrQestnManageList.do?";
        	sLocationUrl = sLocationUrl + "searchMode=" + qustnrQestnManageVO.getSearchMode();
        	sLocationUrl = sLocationUrl + "&qestnrId=" + qustnrQestnManageVO.getQestnrId();
        	sLocationUrl = sLocationUrl + "&qestnrTmplatId=" +qustnrQestnManageVO.getQestnrTmplatId();
		}else{
	     	//공통코드 질문유형 조회
	    	ComDefaultCodeVO voComCode = new ComDefaultCodeVO();
	    	voComCode.setCodeId("COM018");
	    	List listComCode = cmmUseService.selectCmmCodeDetail(voComCode);
	    	model.addAttribute("cmmCode018", listComCode);
	    	
	        List sampleList = egovQustnrQestnManageService.selectQustnrQestnManageDetail(qustnrQestnManageVO);
	        model.addAttribute("resultList", sampleList);
		}
        
		return sLocationUrl; 	
	}
	
	/**
	 * 설문문항를 수정한다. 
	 * @param searchVO
	 * @param commandMap
	 * @param qustnrQestnManageVO
	 * @param bindingResult
	 * @param model
	 * @return "egovframework/com/uss/olp/qqm/EgovQustnrQestnManageModify"
	 * @throws Exception
	 */
	@RequestMapping(value="/uss/olp/qqm/EgovQustnrQestnManageModify.do")
	public String qustnrQestnManageModify(
			@ModelAttribute("searchVO") ComDefaultVO searchVO, 
			@CommandMap Map commandMap,
			@ModelAttribute("qustnrQestnManageVO") QustnrQestnManageVO qustnrQestnManageVO,
			BindingResult bindingResult,
    		ModelMap model)
    throws Exception {
    	// 0. Spring Security 사용자권한 처리
    	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
    	if(!isAuthenticated) {
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.login"));
        	return "egovframework/com/uat/uia/EgovLoginUsr";
    	}
    	
		//로그인 객체 선언
		LoginVO loginVO = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		
		String sLocationUrl = "egovframework/com/uss/olp/qqm/EgovQustnrQestnManageModify"; 

		String sCmd = commandMap.get("cmd") == null ? "" : (String)commandMap.get("cmd");
		
     	//공통코드 질문유형 조회
    	ComDefaultCodeVO voComCode = new ComDefaultCodeVO();
    	voComCode.setCodeId("COM018");
    	List listComCode = cmmUseService.selectCmmCodeDetail(voComCode);
    	model.addAttribute("cmmCode018", listComCode);

        if(sCmd.equals("save")){
    		//서버  validate 체크
            beanValidator.validate(qustnrQestnManageVO, bindingResult);
    		if (bindingResult.hasErrors()){
            	//설문제목가져오기
            	String sQestnrId = commandMap.get("qestnrId") == null ? "" : (String)commandMap.get("qestnrId");
            	String sQestnrTmplatId = commandMap.get("qestnrTmplatId") == null ? "" : (String)commandMap.get("qestnrTmplatId");
            	
            	log.info("sQestnrId =>" + sQestnrId);
            	log.info("sQestnrTmplatId =>" + sQestnrTmplatId);
            	if(!sQestnrId.equals("") && !sQestnrTmplatId.equals("")){
            		
            		Map mapQustnrManage = new HashMap();
            		mapQustnrManage.put("qestnrId", sQestnrId);
            		mapQustnrManage.put("qestnrTmplatId", sQestnrTmplatId);
            		
            		model.addAttribute("qestnrInfo", (Map)egovQustnrQestnManageService.selectQustnrManageQestnrSj(mapQustnrManage));
            	}
            	
                List resultList = egovQustnrQestnManageService.selectQustnrQestnManageDetail(qustnrQestnManageVO);
                model.addAttribute("resultList", resultList);                
            	return "egovframework/com/uss/olp/qqm/EgovQustnrQestnManageModify";
    		}
        	
    		//아이디 설정
    		qustnrQestnManageVO.setFrstRegisterId((String)loginVO.getUniqId());
    		qustnrQestnManageVO.setLastUpdusrId((String)loginVO.getUniqId());
    		
        	egovQustnrQestnManageService.updateQustnrQestnManage(qustnrQestnManageVO);
        	/** 목록으로갈때 검색조건 유지 */
        	sLocationUrl = "redirect:/uss/olp/qqm/EgovQustnrQestnManageList.do?"; 
        	sLocationUrl = sLocationUrl + "searchMode=" + qustnrQestnManageVO.getSearchMode();
        	sLocationUrl = sLocationUrl + "&qestnrId=" + qustnrQestnManageVO.getQestnrId();
        	sLocationUrl = sLocationUrl + "&qestnrTmplatId=" +qustnrQestnManageVO.getQestnrTmplatId();
        }else{
            List resultList = egovQustnrQestnManageService.selectQustnrQestnManageDetail(qustnrQestnManageVO);
            model.addAttribute("resultList", resultList);

        }

		return sLocationUrl; 	
	}
	
	/**
	 * 설문문항를 등록한다.
	 * @param searchVO
	 * @param commandMap
	 * @param qustnrQestnManageVO
	 * @param bindingResult
	 * @param model
	 * @return "egovframework/com/uss/olp/qqm/EgovQustnrQestnManageRegist"
	 * @throws Exception
	 */
	@RequestMapping(value="/uss/olp/qqm/EgovQustnrQestnManageRegist.do")
	public String qustnrQestnManageRegist(
			@ModelAttribute("searchVO") ComDefaultVO searchVO, 
			@CommandMap Map commandMap,  
			@ModelAttribute("qustnrQestnManageVO") QustnrQestnManageVO qustnrQestnManageVO,
			BindingResult bindingResult,
    		ModelMap model)
    throws Exception {
    	// 0. Spring Security 사용자권한 처리
    	Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
    	if(!isAuthenticated) {
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.login"));
        	return "egovframework/com/uat/uia/EgovLoginUsr";
    	}
    	
		//로그인 객체 선언
		LoginVO loginVO = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		
		String sLocationUrl = "egovframework/com/uss/olp/qqm/EgovQustnrQestnManageRegist"; 
		
		String sCmd = commandMap.get("cmd") == null ? "" : (String)commandMap.get("cmd");
		log.info("cmd =>" + sCmd);
		
     	//공통코드 질문유형 조회
    	ComDefaultCodeVO voComCode = new ComDefaultCodeVO();
    	voComCode.setCodeId("COM018");
    	List listComCode = cmmUseService.selectCmmCodeDetail(voComCode);
    	model.addAttribute("cmmCode018", listComCode);

        if(sCmd.equals("save")){
        	
    		//서버  validate 체크
            beanValidator.validate(qustnrQestnManageVO, bindingResult);
    		if (bindingResult.hasErrors()){
            	//설문제목가져오기
            	String sQestnrId = commandMap.get("qestnrId") == null ? "" : (String)commandMap.get("qestnrId");
            	String sQestnrTmplatId = commandMap.get("qestnrTmplatId") == null ? "" : (String)commandMap.get("qestnrTmplatId");
            	
            	log.info("sQestnrId =>" + sQestnrId);
            	log.info("sQestnrTmplatId =>" + sQestnrTmplatId);
            	if(!sQestnrId.equals("") && !sQestnrTmplatId.equals("")){
            		
            		Map mapQustnrManage = new HashMap();
            		mapQustnrManage.put("qestnrId", sQestnrId);
            		mapQustnrManage.put("qestnrTmplatId", sQestnrTmplatId);
            		
            		model.addAttribute("qestnrInfo", (Map)egovQustnrQestnManageService.selectQustnrManageQestnrSj(mapQustnrManage));
            	}
            	
    			return "egovframework/com/uss/olp/qqm/EgovQustnrQestnManageRegist";
    		}
    		
    		//아이디 설정
    		qustnrQestnManageVO.setFrstRegisterId((String)loginVO.getUniqId());
    		qustnrQestnManageVO.setLastUpdusrId((String)loginVO.getUniqId());
    		/** 목록으로갈때 검색조건 유지 */
        	egovQustnrQestnManageService.insertQustnrQestnManage(qustnrQestnManageVO);
        	sLocationUrl = "redirect:/uss/olp/qqm/EgovQustnrQestnManageList.do?";
        	sLocationUrl = sLocationUrl + "searchMode=" + qustnrQestnManageVO.getSearchMode();
        	sLocationUrl = sLocationUrl + "&qestnrId=" + qustnrQestnManageVO.getQestnrId();
        	sLocationUrl = sLocationUrl + "&qestnrTmplatId=" +qustnrQestnManageVO.getQestnrTmplatId();
        	
        }else{
        	
        	//설문제목가져오기
        	String sQestnrId = commandMap.get("qestnrId") == null ? "" : (String)commandMap.get("qestnrId");
        	String sQestnrTmplatId = commandMap.get("qestnrTmplatId") == null ? "" : (String)commandMap.get("qestnrTmplatId");
        	
        	log.info("sQestnrId =>" + sQestnrId);
        	log.info("sQestnrTmplatId =>" + sQestnrTmplatId);
        	if(!sQestnrId.equals("") && !sQestnrTmplatId.equals("")){
        		
        		Map mapQustnrManage = new HashMap();
        		mapQustnrManage.put("qestnrId", sQestnrId);
        		mapQustnrManage.put("qestnrTmplatId", sQestnrTmplatId);
        		
        		model.addAttribute("qestnrInfo", (Map)egovQustnrQestnManageService.selectQustnrManageQestnrSj(mapQustnrManage));
        	}
        	
        }
        
		return sLocationUrl; 
	}
}


