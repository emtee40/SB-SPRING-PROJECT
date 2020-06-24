package sb.project.rest;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import sb.project.domain.Categories;
import sb.project.domain.Items;
import sb.project.repositories.CategoriesRepository;
import sb.project.repositories.ItemsRepository;

import java.util.List;

@Controller
public class ThymeleafUserController {

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private ItemsRepository itemsRepository;

    @GetMapping(value = "/main")
    public String userMainPage(Model model, @RequestParam(value = "selcat", required = false) Long selcat, @ModelAttribute("ctgSel") Categories ctgSel) {
        List<Categories> categoriesList = categoriesRepository.findAll();
        List<Items> itemsList;

        if (selcat == null) {
            itemsList = itemsRepository.findAll();
            model.addAttribute("items", itemsList);
            model.addAttribute("currentCategory", "-");
        } else {
            itemsList = categoriesRepository.findById(selcat).get().getItems();
            model.addAttribute("items", itemsList);
            model.addAttribute("currentCategory", categoriesRepository.findById(selcat).get().getName());
        }
        model.addAttribute("categories", categoriesList);

        for (Items item : itemsList) {
            byte[] image = item.getImage();
            item.setImageString(Base64.encodeBase64String(image));
        }

        for (Categories ctg : categoriesList) {
            byte[] image = ctg.getImage();
            ctg.setImageString(Base64.encodeBase64String(image));
        }

        return "user-main";
    }

    @GetMapping(value = "/main/items/{itemId}")
    public String userItemPage(Model model, @PathVariable long itemId, @ModelAttribute("ctgSel") Categories ctgSel) {
        List<Categories> categoriesList = categoriesRepository.findAll();
        Items item = itemsRepository.findById(itemId);

        model.addAttribute("categories", categoriesList);
        model.addAttribute("currentCategory", item.getCategory().getName());
        model.addAttribute("item", item);

        byte[] image = item.getImage();
        item.setImageString(Base64.encodeBase64String(image));

        for (Categories ctg : categoriesList) {
            byte[] img = ctg.getImage();
            ctg.setImageString(Base64.encodeBase64String(img));
        }

        return "user-main-item";
    }
}