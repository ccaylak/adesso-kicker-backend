package de.adesso.kicker.match.controller;

import de.adesso.kicker.match.persistence.Match;
import de.adesso.kicker.match.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/matches")
public class MatchController {

    private final MatchService matchService;

    @PostMapping("/add")
    public void postAddMatch(@RequestBody @Valid Match match) {
        matchService.addMatchEntry(match);
    }
}
