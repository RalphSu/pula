package pula.sys.services;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import puerta.support.Pe;
import puerta.support.utils.WxlSugar;
import puerta.system.dao.LoggerDao;
import puerta.system.vo.MapBean;
import pula.sys.BhzqConstants;
import pula.sys.daos.MaterialDao;
import pula.sys.daos.SysCategoryDao;
import pula.sys.domains.SysCategory;
import pula.sys.forms.MaterialForm;
import pula.sys.helpers.MaterialHelper;
import pula.sys.vo.MaterialImportAlert;

@Service
public class MaterialService {

	// import
	@Resource
	MaterialDao materialDao;
	@Resource
	SysCategoryDao sysCategoryDao;
	@Resource
	LoggerDao loggerDao;

	public ModelAndView check(List<MaterialForm> afs) {
		Map<String, MapBean> scMap = sysCategoryDao
				.map(BhzqConstants.SC_MATERIAL_TYPE);

		boolean has_error = false;
		Map<String, MaterialImportAlert> changedList = WxlSugar
				.newLinkedHashMap();
		Map<String, MaterialForm> nowStored = WxlSugar.newHashMap();

		for (MaterialForm af : afs) {

			// 编号是否无效
			if (!MaterialHelper.isMaterialNo(af.getNo())) {
				MaterialImportAlert alert = MaterialImportAlert
						.error(af.getNo(), af.getName(), af.getBrand(),
								"编号无效，请遵循编号规则");

				changedList.put(MaterialImportAlert.ERROR_NO + af.getNo(),
						alert);
				has_error = true;
			}

			af.setNo(StringUtils.trim(af.getNo()));

			// 检查 编号是否重复出现
			if (nowStored.containsKey(af.getNo())) {
				MaterialImportAlert alert = MaterialImportAlert.error(
						af.getNo(), af.getName(), af.getBrand(),
						"编号已经存在;或请检查Excel文件中是否有隐藏的行");
				changedList.put(MaterialImportAlert.NO_EXISTS + af.getNo(),
						alert);
				has_error = true;
			}

			// 编号在db中的信息，和当前的命名是否一致 （提醒）
			MapBean existsMapterial = materialDao.mapBeanByNo(af.getNo());
			if (existsMapterial == null) {
				// 新增

				MaterialImportAlert alert = MaterialImportAlert.info(
						af.getNo(), af.getName(), af.getBrand(), "新增材料数据");

				changedList.put(MaterialImportAlert.NEW + af.getNo(), alert);
			} else {
				// 修改
				// 需要找一下他的信息是否有变化，然后给个提示
				String all = existsMapterial.string("materialName").trim()
						+ ":" + existsMapterial.string("spec").trim();

				String this_all = StringUtils.trim(af.getName()) + ":"
						+ StringUtils.trim(af.getBrand());

				if (!StringUtils.equals(all, this_all)) {
					// 新旧信息有所不一致
					MaterialImportAlert alert = MaterialImportAlert.info(
							af.getNo(), af.getName(), af.getBrand(), "修改材料数据:"
									+ all + " 至:" + this_all);

					changedList.put(MaterialImportAlert.EDIT + af.getNo(),
							alert);
				}

			}

			// 分类信息是否能找到

			String no = MaterialHelper.getTypeNo(af.getNo());

			if (!scMap.containsKey(no)) {
				MaterialImportAlert alert = MaterialImportAlert.error(
						af.getNo(), af.getName(), af.getBrand(), "未找到材料分类代码:"
								+ no);

				changedList.put(
						MaterialImportAlert.MISS_MATERIAL_TYPE + af.getNo(),
						alert);
				has_error = true;
			}

			nowStored.put(af.getNo(), af);

		}

		changedList.put(
				MaterialImportAlert.TOTALS,
				MaterialImportAlert.info(null, null, null,
						"导入总数量:" + afs.size()));

		return new ModelAndView().addObject("error", has_error).addObject(
				"alerts", changedList);
	}

	public void save(List<MaterialForm> afs, String file) {

		// 导入或更新

		Map<String, MapBean> scMap = sysCategoryDao
				.map(BhzqConstants.SC_MATERIAL_TYPE);

		for (MaterialForm af : afs) {
			if (!MaterialHelper.isMaterialNo(af.getNo())) {
				Pe.raise("编号无效");
			}

			String no = MaterialHelper.getTypeNo(af.getNo());

			if (!scMap.containsKey(no)) {
				Pe.raise("未找到材料分类代码:" + no);

			}

			MapBean mt = scMap.get(no);

			if (mt != null) {
				af.setCategory(SysCategory.create(mt.string("id")));
				materialDao.saveOrUpdate(af.toMaterial());
			}

		}

		loggerDao.doLog("导入材料", file);

	}
}
