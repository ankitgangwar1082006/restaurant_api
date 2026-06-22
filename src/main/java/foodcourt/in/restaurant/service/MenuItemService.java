package foodcourt.in.restaurant.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import foodcourt.in.restaurant.dto.MenuItemRequestDto;
import foodcourt.in.restaurant.dto.MenuItemResponseDto;
import foodcourt.in.restaurant.dto.MenuPageResponseDto;
import foodcourt.in.restaurant.entity.MenuItem;
import foodcourt.in.restaurant.entity.Restaurant;
import foodcourt.in.restaurant.exception.ResourceNotFoundException;
import foodcourt.in.restaurant.repository.MenuItemRepository;
import foodcourt.in.restaurant.repository.RestaurantRepository;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MenuItemService {

    MenuItemRepository menuItemRepository;
    RestaurantRepository restaurantRepository;
    MenuItemService (MenuItemRepository menuItemRepository ,
                     RestaurantRepository restaurantRepository){
        this.restaurantRepository=restaurantRepository;
        this.menuItemRepository=menuItemRepository;
    }

    public MenuItemResponseDto saveMenu(MenuItemRequestDto requestDto)
    {
        MenuItem menuItem = new MenuItem();

        menuItem.setName(requestDto.getName());
        menuItem.setPrice(requestDto.getPrice());

        Restaurant restaurant = restaurantRepository.
                findById(requestDto.getRestaurantId()).
                orElseThrow(()->new ResourceNotFoundException(" RestaurantId is wrong"));

        menuItem.setRestaurant(restaurant);
        MenuItem savedMenuItem=menuItemRepository.save(menuItem);

        return createMenuResponse(savedMenuItem);
    }
    public MenuItemResponseDto updateMenu(Long id , MenuItemRequestDto requestDto)
    {
        MenuItem menuItem = menuItemRepository.findById(id).
                orElseThrow(()-> new ResourceNotFoundException("Invalid/Not exist Id"));

        menuItem.setName(requestDto.getName());
        menuItem.setPrice(requestDto.getPrice());

        Restaurant restaurant = restaurantRepository.
                findById(requestDto.getRestaurantId()).
                orElseThrow(()->new ResourceNotFoundException("Invalid/Not Exist"));

        menuItem.setRestaurant(restaurant);
        MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
        return createMenuResponse(updatedMenuItem);
    }
    public void deleteMenu(Long id)
    {
        MenuItem menuItem = menuItemRepository.
                findById(id).
                orElseThrow(()->new ResourceNotFoundException("Invalid/did'nt exist id "));
        menuItemRepository.delete(menuItem);
    }
    public MenuItemResponseDto getMenu(Long id)
    {
        MenuItemResponseDto responseDto = new MenuItemResponseDto();
        MenuItem menuItem = menuItemRepository.
                findById(id).
                orElseThrow(()->new ResourceNotFoundException("Invalid Id"));
        return createMenuResponse(menuItem);
    }
    public MenuPageResponseDto getMenuItemsByPage(int pageNo , int size)
    {
        Pageable pageable = PageRequest.of(pageNo,size);
        Page<MenuItem> page = menuItemRepository.findAll(pageable);

        List<MenuItem> list = page.getContent();
        List<MenuItemResponseDto> dtoList = new ArrayList<>();
        for(MenuItem item :list)
        {
            dtoList.add(createMenuResponse(item));
        }
        MenuPageResponseDto pageResponseDto = new MenuPageResponseDto();

        pageResponseDto.setPageNo(page.getNumber());
        pageResponseDto.setPageSize(page.getSize());
        pageResponseDto.setTotalPages(page.getTotalPages());
        pageResponseDto.setLast(page.isLast());
        pageResponseDto.setContent(dtoList);
        pageResponseDto.setTotalElements(page.getTotalElements());
        return pageResponseDto;
    }
    public MenuItemResponseDto createMenuResponse(MenuItem menu)
    {
        MenuItemResponseDto responseDto = new MenuItemResponseDto();
        responseDto.setId(menu.getId());
        responseDto.setPrice(menu.getPrice());
        responseDto.setName(menu.getName());
        responseDto.setImgUrl(menu.getImageUrl());
        if (menu.getRestaurant() != null) {
            responseDto.setRestaurantId(menu.getRestaurant().getId());
        }
        return responseDto;
    }

    public void uploadImg(Long id , MultipartFile file) throws IOException {
        MenuItem menuItem = menuItemRepository.
                findById(id).
                orElseThrow(()->new ResourceNotFoundException("menu do not exists with id:"+id));

        final String uploadDir=System.getProperty("user.dir")+"/uploads/";

        File directory = new File(uploadDir);
        if(!directory.exists())
        {
            directory.mkdir();
        }

        String originalName=file.getOriginalFilename();
        String randomID = System.currentTimeMillis() + "_" + UUID.randomUUID().toString();
        String newName= randomID+originalName.substring(originalName.lastIndexOf('.'));

        String fullPath=uploadDir+File.separator + newName;

        Files.copy(file.getInputStream(), Paths.get(fullPath), StandardCopyOption.REPLACE_EXISTING);

        menuItem.setImageUrl(newName);
        menuItemRepository.save(menuItem);
    }
}
