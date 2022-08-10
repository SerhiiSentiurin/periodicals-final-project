package periodicals.epam.com.project.logic.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import periodicals.epam.com.project.logic.dao.ReaderDAO;
import periodicals.epam.com.project.logic.entity.Reader;
import periodicals.epam.com.project.logic.entity.dto.ReaderCreateDTO;
import periodicals.epam.com.project.logic.logicExeption.ReaderException;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReaderServiceTest {
    @Mock
    private ReaderDAO dao;

    @InjectMocks
    private ReaderService readerService;

    private static final Long READER_ID  = 1L;

    @Test
    public void createReaderTest(){
        ReaderCreateDTO dto = new ReaderCreateDTO(READER_ID,"login","password");
        Reader expectedReader = new Reader();
        expectedReader.setId(dto.getId());
        expectedReader.setLogin(dto.getLogin());

        when(dao.insertReader(dto)).thenReturn(dto);

        Reader resultReader = readerService.createReader(dto);
        assertEquals(expectedReader,resultReader);
    }

    @Test
    public void getReaderByIdWhenReaderFindTest(){
        Reader expectedReader = new Reader();
        expectedReader.setId(READER_ID);
        Optional<Reader> expectedOptional = Optional.of(expectedReader);

        when(dao.getReaderById(READER_ID)).thenReturn(expectedOptional);
        Reader resultReader = readerService.getReaderById(READER_ID);
        assertEquals(expectedReader,resultReader);
    }

    @Test(expected = ReaderException.class)
    public void getReaderByIdWhenReaderNotFoundTest(){
        when(dao.getReaderById(READER_ID)).thenReturn(Optional.empty());
        readerService.getReaderById(READER_ID);
    }

}
