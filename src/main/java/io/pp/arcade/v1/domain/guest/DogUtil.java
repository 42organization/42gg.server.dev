package io.pp.arcade.v1.domain.guest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DogUtil {
    public DogUtil() {

    }

    @Value("${info.image.dogUrl}")
    private String dogImageUrl;

    static String[] dogs = {
            "beagle",
            "bulldog",
            "doberman",
            "poodle",
            "bichon",
            "corgi",
            "pomeranian",
            "retriever"
    };

    public void test() {
        System.out.println("dogImageUrl = " + dogImageUrl);
    }
    public String getRandomDog() {
        return dogs[(int) (Math.random() * dogs.length)];
    }

    public String getRandomDogImage(String dog) {
        return dogImageUrl + dog + ".png";
    }

    public String getRandomDogName(String dog) {
        String koreanDog;
        switch (dog) {
            case "beagle":
                koreanDog = "비글";
                break;
            case "bulldog":
                koreanDog = "불독";
                break;
            case "doberman":
                koreanDog = "도베르만";
                break;
            case "poodle":
                koreanDog = "푸들";
                break;
            case "bichon":
                koreanDog = "비숑";
                break;
            case "corgi":
                koreanDog = "코기";
                break;
            case "pomeranian":
                koreanDog = "포메라니안";
                break;
            case "retriever":
                koreanDog = "리트리버";
                break;
            default:
                koreanDog = "비글";
        }
        return DogAdjectives.getRandomAdjective() + koreanDog;
    }
}
