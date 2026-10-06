package com.HolidayService.Service;

import com.HolidayService.Dto.HolidayRequestDto;
import com.HolidayService.Dto.HolidayResponseDto;
import com.HolidayService.Entity.Holiday;
import com.HolidayService.EnumClass.HolidayAction;
import com.HolidayService.Repository.HolidayRepository;
import com.HolidayService.common.PagedResponse;
import com.HolidayService.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HolidayService
{
    private final ModelMapper mapper;
    private final HolidayRepository holidayRepository;

    // create
    public HolidayResponseDto create(HolidayRequestDto requestDto){
        Holiday holiday = mapper.map(requestDto, Holiday.class);
        holiday.setAction("PENDING");
        Holiday saved = holidayRepository.save(holiday);
        HolidayResponseDto map = mapper.map(saved, HolidayResponseDto.class);
        return map;

    }

    // getAll
    public PagedResponse<HolidayResponseDto> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Page<Holiday> holidayPage = holidayRepository.findAll(pageable);

        List<HolidayResponseDto> content = holidayPage
                .stream()
                .map(holiday -> mapper.map(holiday, HolidayResponseDto.class))
                .collect(Collectors.toList());

        return new PagedResponse<>(
                content,
                holidayPage.getNumber(),
                holidayPage.getSize(),
                holidayPage.getTotalElements(),
                holidayPage.getTotalPages(),
                holidayPage.isLast()
        );
    }


    // getById
    public HolidayResponseDto getById(Long id){
        Holiday holiday = holidayRepository.findById(id).orElseThrow(() -> new ResolutionException("Id not found"));
        return mapper.map(holiday, HolidayResponseDto.class);
    }

    // update
    public HolidayResponseDto update(Long id,HolidayRequestDto requestDto){
        Holiday holiday = holidayRepository.findById(id).orElseThrow(() -> new ResolutionException("Id not found"));
      if(holiday.getTitle()!=null){
          holiday.setTitle(requestDto.getTitle());
      }
      if(holiday.getDate()!=null){
          holiday.setDate(requestDto.getDate());
      }
      if(holiday.getDescription()!=null){
          holiday.setDescription(requestDto.getDescription());
      }
        Holiday saved = holidayRepository.save(holiday);
        return mapper.map(saved, HolidayResponseDto.class);
    }

    // delete
    public void delete(Long id){
        Holiday holiday = holidayRepository.findById(id).orElseThrow(() -> new ResolutionException("Id not found"));
        holidayRepository.delete(holiday);
    }

    // searchByTitle
    public HolidayResponseDto searchByTitle(String title){
        Holiday byTitle = holidayRepository.findByTitle(title);
        if(byTitle==null){
            throw new ResourceNotFoundException("Title not found");
        }
        return mapper.map(byTitle, HolidayResponseDto.class);
    }

    public HolidayResponseDto updateAction(Long id, String action) {
        if (!EnumUtils.isValidEnum(HolidayAction.class, action.toUpperCase())) {
            throw new IllegalArgumentException("Invalid action value");
        }
        Holiday holiday = holidayRepository.findById(id)
                .orElseThrow(() -> new ResolutionException("Holiday with ID " + id + " not found"));

        holiday.setAction(action.toUpperCase());

        holiday.setAction(action);

        Holiday updated = holidayRepository.save(holiday);
        return mapper.map(updated, HolidayResponseDto.class);
    }

}
