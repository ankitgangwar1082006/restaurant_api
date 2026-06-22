package foodcourt.in.restaurant.controller;

import foodcourt.in.restaurant.dto.MenuItemRequestDto;
import foodcourt.in.restaurant.dto.MenuItemResponseDto;
import foodcourt.in.restaurant.dto.MenuPageResponseDto;
import foodcourt.in.restaurant.service.MenuItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/menu_items")
public class MenuItemController {

    MenuItemService service;

    MenuItemController(MenuItemService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<MenuItemResponseDto> createMenuItem(@RequestBody @Valid MenuItemRequestDto requestDto) {
        MenuItemResponseDto response = service.saveMenu(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<String> uploadMenuItemImg(@PathVariable("id") Long id ,
                                                    @RequestParam("image") MultipartFile file) throws IOException {
        service.uploadImg(id,file);
        return new ResponseEntity<>("uploaded img SuccessFully",HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMenu(@PathVariable("id") Long id) {
        service.deleteMenu(id);
        return new ResponseEntity<>("Menu Deleted Successfully", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemResponseDto> updateMenu(@PathVariable("id") Long id,
                                                          @RequestBody @Valid MenuItemRequestDto requestDto) {
        MenuItemResponseDto response = service.updateMenu(id, requestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemResponseDto> getMenuItemById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(service.getMenu(id), HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<MenuPageResponseDto> getAllMenuItems(@RequestParam(defaultValue = "0")int page ,
                                                               @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(service.getMenuItemsByPage(page,size), HttpStatus.OK);
    }
}