/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.Service;

/**
 *
 * @author Haruki
 */
import deu.se.SentiDiary.Entity.Weather;
import deu.se.SentiDiary.Repository.WeatherRepository;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    private final WeatherRepository weatherRepository;

    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public Weather getById(Long id) {
        return weatherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("날씨 정보가 존재하지 않습니다."));
    }
}
