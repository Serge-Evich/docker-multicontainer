package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class FibonacciServiceTest {

    @InjectMocks
    private FibonacciService fibonacciService;

    @Test
    public void shouldReturn8() {
        assertThat(fibonacciService.fib(5)).isEqualTo(8);
    }
}