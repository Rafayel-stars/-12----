package org.example.bookmanagesystem01.mapper;

import org.example.bookmanagesystem01.entity.LibrarySpace;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LibrarySpaceMapper {
    LibrarySpace getLibrarySpaceById(int spaceId);
    void addLibrarySpace(LibrarySpace librarySpace);
    void updateLibrarySpace(LibrarySpace librarySpace);
    void deleteLibrarySpace(int spaceId);

    List<LibrarySpace> selectAll();
}