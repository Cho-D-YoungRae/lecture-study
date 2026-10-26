package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
@Slf4j
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;

    private final ItemValidator itemValidator;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

    /**
     * ValidationItemControllerV1 에서 Map 사용하던 것을 BindingResult 사용하도록
     */
//    @PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        // 검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError(
                    "item", "itemName", "상품 이름은 필수입니다."));
        }
        if (isNull(item.getPrice()) || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError(
                    "item", "price", "가격은 1,000 ~ 1,000,000 까지 허용합니다."));

        }
        if (isNull(item.getQuantity()) || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError(
                    "item", "quantity",  "수량은 9,999 까지 허용합니다."));
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (nonNull(item.getPrice()) && nonNull(item.getQuantity())) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError(
                        "item",  "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재값 = " + resultPrice));
            }
        }

        if (bindingResult.hasErrors()) {
            // 검증에 실패하면 다시 입력 폼으로
            log.info("errors = {}", bindingResult);
            /* // bindingResult 는 자동으로 view 에 넘어가기 때문에 아래와 같은 로직 불필요
            model.addAttribute("errors", errors);
            */
            return "validation/v2/addForm";
        }

        // 성공로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    /**
     * V1 에서는 오류가 나면 해당 필드의 값이 날아갔다.
     * 오류가 나도 해당 필드에 값이 날아가지 않도록
     */
//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        // 검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError(
                    "item", "itemName", item.getItemName(),
                    false, null, null,
                    "상품 이름은 필수입니다."));
        }
        if (isNull(item.getPrice()) || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError(
                    "item", "price", item.getPrice(),
                    false, null, null,
                    "가격은 1,000 ~ 1,000,000 까지 허용합니다."));

        }
        if (isNull(item.getQuantity()) || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError(
                    "item", "quantity", item.getQuantity(),
                    false, null, null,
                    "수량은 9,999 까지 허용합니다."));
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (nonNull(item.getPrice()) && nonNull(item.getQuantity())) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError(
                        "item", null, null,
                        "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재값 = " + resultPrice));
            }
        }

        if (bindingResult.hasErrors()) {
            // 검증에 실패하면 다시 입력 폼으로
            log.info("errors = {}", bindingResult);
            /* // bindingResult 는 자동으로 view 에 넘어가기 때문에 아래와 같은 로직 불필요
            model.addAttribute("errors", errors);
            */
            return "validation/v2/addForm";
        }

        // 성공로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    /**
     * errorCode 를 통해 error message 적용
     */
//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError(
                    "item", "itemName", item.getItemName(),
                    false, new String[]{"required.item.itemName"}, null,
                    null));
        }
        if (isNull(item.getPrice()) || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError(
                    "item", "price", item.getPrice(),
                    false, new String[]{"range.item.price"}, new Object[]{1000, 1000000},
                    null));

        }
        if (isNull(item.getQuantity()) || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError(
                    "item", "quantity", item.getQuantity(),
                    false, new String[]{"max.item.quantity"}, new Object[]{9999},
                    null));
        }

        if (nonNull(item.getPrice()) && nonNull(item.getQuantity())) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError(
                        "item", new String[]{"totalPriceMin"}, new Object[]{10000, resultPrice},
                        null));
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }

        // 성공로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    /**
     * FieldError, ObjectError => rejectValue, reject 를 통해서 코드 단순화
     */
//    @PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        log.info("objectName = {}", bindingResult.getObjectName());
        log.info("target = {}", bindingResult.getTarget());

        // typeMismatch 가 있는 경우 아래 검증 없이 바로 되돌려 보내도록
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }

        // 아래와 같은 기능
        // ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName", "required");
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.rejectValue("itemName", "required");
        }
        if (isNull(item.getPrice()) || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.rejectValue("price", "range", new Object[]{1000, 1000000},
                    null);

        }
        if (isNull(item.getQuantity()) || item.getQuantity() >= 9999) {
            bindingResult.rejectValue("quantity", "max", new Object[]{9999},
                    null);

        }

        if (nonNull(item.getPrice()) && nonNull(item.getQuantity())) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice},
                        null);
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }

        // 성공로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    /**
     * Validator 분리
     */
//    @PostMapping("/add")
    public String addItemV5(@ModelAttribute Item item, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        if (itemValidator.supports(item.getClass())) {
            itemValidator.validate(item, bindingResult);
        }

        log.info("objectName = {}", bindingResult.getObjectName());
        log.info("target = {}", bindingResult.getTarget());

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @InitBinder
    public void init(WebDataBinder dataBinder) {
        dataBinder.addValidators(itemValidator);
    }

    /**
     * WebDataBinder 를 통해서 Validator 직접 호출하지 않도록
     */
    @PostMapping("/add")
    public String addItemV6(@Validated @ModelAttribute Item item, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        log.info("objectName = {}", bindingResult.getObjectName());
        log.info("target = {}", bindingResult.getTarget());

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

}

