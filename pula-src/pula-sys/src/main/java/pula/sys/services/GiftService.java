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
import pula.sys.daos.GiftDao;
import pula.sys.daos.SysCategoryDao;
import pula.sys.domains.SysCategory;
import pula.sys.forms.GiftForm;
import pula.sys.helpers.GiftHelper;
import pula.sys.vo.GiftImportAlert;

@Service
public class GiftService {

	// import
	@Resource
	GiftDao giftDao;
	@Resource
	SysCategoryDao sysCategoryDao;
	@Resource
	LoggerDao loggerDao;

	public ModelAndView check(List<GiftForm> afs) {
		Map<String, MapBean> scMap = sysCategoryDao
				.map(BhzqConstants.SC_GIFT_TYPE);

		boolean has_error = false;
		Map<String, GiftImportAlert> changedList = WxlSugar
				.newLinkedHashMap();
		Map<String, GiftForm> nowStored = WxlSugar.newHashMap();

		for (GiftForm af : afs) {

			// 编号是否无效
			if (!GiftHelper.isGiftNo(af.getNo())) {
				GiftImportAlert alert = GiftImportAlert
						.error(af.getNo(), af.getName(), af.getBrand(),
								"编号无效，请遵循编号规则");

				changedList.put(GiftImportAlert.ERROR_NO + af.getNo(),
						alert);
				has_error = true;
			}

			af.setNo(StringUtils.trim(af.getNo()));

			// 检查 编号是否重复出现
			if (nowStored.containsKey(af.getNo())) {
				GiftImportAlert alert = GiftImportAlert.error(
						af.getNo(), af.getName(), af.getBrand(),
						"编号已经存在;或请检查Excel文件中是否有隐藏的行");
				changedList.put(GiftImportAlert.NO_EXISTS + af.getNo(),
						alert);
				has_error = true;
			}

			// 编号在db中的信息，和当前的命名是否一致 （提醒）
			MapBean existsMapterial = giftDao.mapBeanByNo(af.getNo());
			if (existsMapterial == null) {
				// 新增

				GiftImportAlert alert = GiftImportAlert.info(
						af.getNo(), af.getName(), af.getBrand(), "新增礼品数据");

				changedList.put(GiftImportAlert.NEW + af.getNo(), alert);
			} else {
				// 修改
				// 需要找一下他的信息是否有变化，然后给个提示
				String all = existsMapterial.string("giftName").trim()
						+ ":" + existsMapterial.string("spec").trim();

				String this_all = StringUtils.trim(af.getName()) + ":"
						+ StringUtils.trim(af.getBrand());

				if (!StringUtils.equals(all, this_all)) {
					// 新旧信息有所不一致
					GiftImportAlert alert = GiftImportAlert.info(
							af.getNo(), af.getName(), af.getBrand(), "修改礼品数据:"
									+ all + " 至:" + this_all);

					changedList.put(GiftImportAlert.EDIT + af.getNo(),
							alert);
				}

			}

			// 分类信息是否能找到

			String no = GiftHelper.getTypeNo(af.getNo());

			if (!scMap.containsKey(no)) {
				GiftImportAlert alert = GiftImportAlert.error(
						af.getNo(), af.getName(), af.getBrand(), "未找到礼品分类代码:"
								+ no);

				changedList.put(
						GiftImportAlert.MISS_GIFT_TYPE + af.getNo(),
						alert);
				has_error = true;
			}

			nowStored.put(af.getNo(), af);

		}

		changedList.put(
				GiftImportAlert.TOTALS,
				GiftImportAlert.info(null, null, null,
						"导入总数量:" + afs.size()));

		return new ModelAndView().addObject("error", has_error).addObject(
				"alerts", changedList);
	}

	public void save(List<GiftForm> afs, String file) {

		// 导入或更新

		Map<String, MapBean> scMap = sysCategoryDao
				.map(BhzqConstants.SC_GIFT_TYPE);

		for (GiftForm af : afs) {
			if (!GiftHelper.isGiftNo(af.getNo())) {
				Pe.raise("编号无效");
			}

			String no = GiftHelper.getTypeNo(af.getNo());

			if (!scMap.containsKey(no)) {
				Pe.raise("未找到礼品分类代码:" + no);

			}

			MapBean mt = scMap.get(no);

			if (mt != null) {
				af.setCategory(SysCategory.create(mt.string("id")));
				giftDao.saveOrUpdate(af.toGift());
			}

		}

		loggerDao.doLog("导入礼品", file);

	}
}
