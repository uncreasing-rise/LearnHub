package com.example.learnhub.Service;

import com.example.learnhub.DTO.WishlistDTO;
import org.springframework.data.crossstore.ChangeSetPersister;

public interface IServiceOfWishlist {
    WishlistDTO addToWishList(WishlistDTO wishlistDTO);
    WishlistDTO deleteWishlistItem(Integer wishlistId) throws ChangeSetPersister.NotFoundException;

}
