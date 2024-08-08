package com.maids.librarymanagementsystem.service;

import com.maids.librarymanagementsystem.dto.PatronTO;
import com.maids.librarymanagementsystem.entity.Patron;
import com.maids.librarymanagementsystem.exception.model.BadRequestException;
import com.maids.librarymanagementsystem.exception.model.ConflictException;
import com.maids.librarymanagementsystem.exception.model.ResourceNotFoundException;
import com.maids.librarymanagementsystem.repository.BorrowingRecordRepository;
import com.maids.librarymanagementsystem.repository.PatronRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class PatronServiceImpl implements PatronService{
    private PatronRepository patronRepository;
    private BorrowingRecordRepository borrowingRecordRepository;

    public PatronServiceImpl(PatronRepository patronRepository, BorrowingRecordRepository borrowingRecordRepository) {
        this.patronRepository = patronRepository;
        this.borrowingRecordRepository = borrowingRecordRepository;
    }

    @Cacheable("getAllPatrons")
    @Override
    public List<PatronTO> getAllPatrons() {
        List<Patron>patronList=patronRepository.findAll();
        List<PatronTO>patronTOList=new ArrayList<>();
        if(patronList.size()==0)
            return patronTOList;
        for(Patron patron:patronList){
            patronTOList.add(convertToTO(patron));
        }
        return patronTOList;
    }

    @Cacheable("getPatronById")
    @Override
    public PatronTO getPatronById(Long id) throws ResourceNotFoundException {
        try {
            Patron patron=patronRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("no Patron found with id = "+id));
            return convertToTO(patron);
        }catch (ResourceNotFoundException resourceNotFoundException){
            throw resourceNotFoundException;
        }catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }

    @Transactional
    @Override
    public PatronTO addPatron(PatronTO patronTO) throws BadRequestException, ConflictException {
        try {
            if(patronTO.getContactInfo()==null || patronTO.getContactInfo().trim().isEmpty() ||
                    patronTO.getName()==null || patronTO.getName().trim().isEmpty()){
                throw new BadRequestException("Invalid Patron Data");
            }
            if(patronRepository.findByContact(patronTO.getContactInfo())!=null){
                throw new ConflictException("contact already added for other patron");
            }
            Patron patron=convertToEntity(patronTO);
            return convertToTO(patronRepository.save(patron));
        }catch (BadRequestException badRequestException){
            throw badRequestException;
        }catch (ConflictException conflictException){
            throw conflictException;
        }catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }

    }

    @Transactional
    @Override
    public PatronTO updatePatron(Long id,PatronTO patronTO) throws ResourceNotFoundException, ConflictException {
        try {
            Patron oldpatron= patronRepository.findById(id).
                    orElseThrow(()->new ResourceNotFoundException ("no Patron found with id = "+id));
            if(patronRepository.findByContactForExistingPatron(patronTO.getContactInfo(),id)!=null){
                throw new ConflictException("contact already added for other patron");
            }
            if(patronTO.getName()!=null || !patronTO.getName().trim().isEmpty()){
                oldpatron.setName(patronTO.getName());
            }if(patronTO.getContactInfo()!=null || !patronTO.getContactInfo().trim().isEmpty()){
                oldpatron.setContactInfo(patronTO.getContactInfo());
            }
            return convertToTO(patronRepository.save(oldpatron));
        }catch (ResourceNotFoundException resourceNotFoundException){
            throw resourceNotFoundException;
        }catch (ConflictException conflictException){
            throw conflictException;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional
    @Override
    public String removePatron(Long id) throws ResourceNotFoundException, ConflictException {
        try {
            Patron patron= patronRepository.findById(id).
                    orElseThrow(()->new ResourceNotFoundException ("no Patron found with id = "+id));
            int count=borrowingRecordRepository.getBorrowingRecordsCountByPatronId(id);
            if(count==0){
                patronRepository.delete(patron);
                return "Patron Deleted Successfully!";
            }else {
                throw new ConflictException("cannot remove patron until he returns his books");
            }
        }catch (ResourceNotFoundException resourceNotFoundException){
            throw resourceNotFoundException;
        }catch (ConflictException conflictException){
            throw conflictException;
        }catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }

    //    private ModelMapper modelMapper;
    public PatronTO convertToTO(Patron patron){
        PatronTO patronTO=new PatronTO();
        patronTO.setId(patron.getId());
        patronTO.setContactInfo(patron.getContactInfo());
        patronTO.setName(patron.getName());
        return patronTO;
    }

    public Patron convertToEntity(PatronTO patronTO){
        Patron patron=new Patron();
        patron.setId(patronTO.getId());
        patron.setContactInfo(patronTO.getContactInfo());
        patron.setName(patronTO.getName());
        return patron;
    }
}
