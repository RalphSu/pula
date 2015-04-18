package pula.sys.app;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import puerta.PuertaWeb;
import puerta.support.PaginationSupport;
import puerta.support.Pe;
import puerta.support.ViewResult;
import puerta.support.annotation.Barrier;
import puerta.support.annotation.ObjectParam;
import puerta.support.utils.DateExTool;
import puerta.support.utils.WxlSugar;
import puerta.support.vo.NoNameVo;
import puerta.support.vo.SelectOptionList;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.service.SessionService;
import puerta.system.vo.JsonResult;
import puerta.system.vo.MapBean;
import puerta.system.vo.YuiResult;
import puerta.system.vo.YuiResultMapper;
import pula.sys.BhzqConstants;
import pula.sys.PurviewConstants;
import pula.sys.conditions.MaterialCondition;
import pula.sys.daos.MaterialDao;
import pula.sys.daos.SysCategoryDao;
import pula.sys.domains.Material;
import pula.sys.domains.SysCategory;
import pula.sys.forms.MaterialForm;
import pula.sys.helpers.MaterialHelper;
import pula.sys.services.MaterialService;

@Controller
public class MaterialController {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Material.class);
	private static final YuiResultMapper<Material> MAPPING = new YuiResultMapper<Material>() {
		@Override
		public Map<String, Object> toMap(Material obj) {

			Map<String, Object> m = WxlSugar.newHashMap();
			m.put("no", obj.getNo());
			m.put("id", obj.getId());
			m.put("name", obj.getName());
			if (obj.getCategory() != null) {
				m.put("categoryName", obj.getCategory().getName());
			} else {

			}
			m.put("superficialArea", obj.getSuperficialArea());
			m.put("weight", obj.getWeight());
			m.put("raw", obj.getRaw());
			m.put("brand", obj.getBrand());
			m.put("unit", obj.getUnit());

			return m;
		}
	};

	private static final YuiResultMapper<Material> MAPPING_FULL = new YuiResultMapper<Material>() {
		@Override
		public Map<String, Object> toMap(Material obj) {

			Map<String, Object> m = MAPPING.toMap(obj);

			if (obj.getCategory() != null) {
				m.put("categoryId", obj.getCategory().getId());
			} else {

			}

			m.put("pinyin", obj.getPinyin());
			return m;
		}
	};

	@Resource
	MaterialDao materialDao;
	@Resource
	ParameterKeeper parameterKeeper;
	@Resource
	SessionService sessionService;
	@Resource
	SysCategoryDao sysCategoryDao;
	@Resource
	MaterialService materialService;

	// @Resource
	// MaterialService materialService;

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.MATERIAL)
	public ModelAndView entry(
			@ObjectParam("condition") MaterialCondition condition) {
		if (condition == null) {
			condition = new MaterialCondition();
		}

		SelectOptionList enabled_statusList = PuertaWeb.getYesNoList(0,
				new String[] { "启用", "禁用" });
		List<SysCategory> mts = sysCategoryDao
				.getUnder(BhzqConstants.SC_MATERIAL_TYPE);
		return new ModelAndView().addObject("condition", condition)
				.addObject("types", mts)
				.addObject("enabledStatusList", enabled_statusList);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.MATERIAL)
	public YuiResult list(
			@ObjectParam("condition") MaterialCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new MaterialCondition();
		}
		PaginationSupport<MapBean> results = null;
		results = materialDao.searchMapBean(condition, pageIndex);
		return YuiResult.create(results);
	}

	@RequestMapping
	@Barrier(PurviewConstants.MATERIAL)
	public String create() {
		return null;
	}

	@RequestMapping
	@Transactional
	@Barrier(PurviewConstants.MATERIAL)
	public String _create(@ObjectParam("material") MaterialForm cli) {

		Material cc = cli.toMaterial();

		materialDao.save(cc);
		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional()
	@Barrier(PurviewConstants.MATERIAL)
	public String _update(@ObjectParam("material") MaterialForm cli) {

		Material cc = cli.toMaterial();
		materialDao.update(cc);
		return ViewResult.JSON_SUCCESS;
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.MATERIAL)
	public String remove(
			@RequestParam(value = "objId", required = false) Long[] id) {
		materialDao.deleteById(id);
		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.MATERIAL)
	public JsonResult get(@RequestParam("id") Long id) {
		MapBean u = materialDao.unique(id);
		return JsonResult.s(u);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier
	public JsonResult getByNo(@RequestParam("no") String no) {
		Material u = materialDao.findByNo(no);
		if (u == null) {
			Pe.raise("找不到指定的编号:" + no);
		}
		return JsonResult.s(MAPPING_FULL.toMap(u));
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier
	public ModelAndView find(
			@RequestParam(value = "no", required = false) String no) {
		ModelAndView mv = new ModelAndView(ViewResult.JSON_LIST);
		List<Material> list = materialDao.loadByKeywords(no);
		logger.debug("list.size=" + list.size());

		List<NoNameVo> results = WxlSugar.newArrayList(list.size());
		for (Material m : list) {
			results.add(NoNameVo.create(String.valueOf(m.getId()), m.getNo(),
					m.getName()));
		}

		mv.addObject("list", results);
		return mv;
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.MATERIAL)
	public String enable(
			@RequestParam(value = "objId", required = false) Long[] id,
			@RequestParam(value = "enable", required = false) boolean enable) {
		materialDao.doEnable(id, enable);
		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping("/suggest")
	@ResponseBody
	@Barrier
	@Transactional(readOnly = false, isolation = Isolation.READ_UNCOMMITTED)
	public Map<String, Object> suggest(@RequestParam("query") String q) {
		q = org.apache.commons.lang.StringUtils.trim(q);
		logger.debug("query=[" + q + "]");
		List<Material> sug = materialDao.loadSuggest(q, 10);
		Map<String, Object> m = WxlSugar.newHashMap();
		List<Map<String, Object[]>> r = WxlSugar.newArrayList(sug.size());
		m.put("query", q);
		int i = 0;
		for (Material s : sug) {
			Map<String, Object[]> mr = WxlSugar.newHashMap();
			mr.put(String.valueOf(i),
					new Object[] { s.getNo(), s.getName(), s.getUnit() });
			r.add(mr);
		}

		logger.debug("result=" + r.size());
		m.put("suggestions", r);
		return m;
	}

	/**
	 * 导入BOM
	 * 
	 * @param id
	 * @param file
	 * @return
	 */
	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.MATERIAL_IMPORT)
	public ModelAndView importMaterialCheck(
			@RequestParam(value = "file", required = false) MultipartFile file) {

		if (file == null) {
			Pe.raise("无效的文件");
		}

		List<MaterialForm> afs = null;
		try {
			afs = MaterialHelper.read(file.getInputStream());

		} catch (Exception e) {

			e.printStackTrace();
			return Pe.raise("无效的文件:" + e.getMessage());
		}

		ModelAndView mav = materialService.check(afs);

		// backup
		boolean error = (Boolean) mav.getModel().get("error");

		if (!error) {

			String path = parameterKeeper
					.getFilePath(BhzqConstants.FILE_MATERIAL_DIR);

			String basePath = "/" + DateExTool.getToday() + "/";
			basePath += String
					.valueOf(Calendar.getInstance().getTimeInMillis()) + ".xls";
			try {
				FileUtils.forceMkdir(new File(path));
				file.transferTo(new File(path + basePath));

				mav.addObject("file", basePath);

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}

		return mav;

	}

	/**
	 * 导入BOM-入口
	 * 
	 * @param id
	 * @param file
	 * @return
	 */
	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.MATERIAL_IMPORT)
	public ModelAndView importMaterial() {

		return null;

	}

	/**
	 * 导入BOM
	 * 
	 * @param id
	 * @param file
	 * @return
	 */
	@RequestMapping
	@Transactional
	@ResponseBody
	@Barrier(PurviewConstants.MATERIAL_IMPORT)
	public JsonResult _importMaterial(@RequestParam(value = "file") String file) {

		if (file == null) {
			Pe.raise("无效的文件");
		}

		List<MaterialForm> afs = null;
		String path = parameterKeeper
				.getFilePath(BhzqConstants.FILE_MATERIAL_DIR);
		try {
			afs = MaterialHelper.read(path + file);

		} catch (Exception e) {

			e.printStackTrace();
			return Pe.raise("无效的文件:" + e.getMessage());
		}

		materialService.save(afs, file);

		return JsonResult.s();

	}

}
