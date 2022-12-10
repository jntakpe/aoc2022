use std::collections::HashSet;

use aoc_2022::shared::{read_input_lines, Day};

fn main() {
    Day3 {
        input: read_input_lines(3),
    }
    .run()
}

struct Day3 {
    input: Vec<String>,
}

impl Day3 {
    fn position(c: char) -> usize {
        ('a'..='z').chain('A'..='Z').position(|r| r == c).unwrap() + 1
    }
}

impl Day<usize> for Day3 {
    fn part1(&self) -> usize {
        self.input
            .iter()
            .map(|s| s.split_at(s.len() / 2))
            .map(|(s1, s2)| {
                let mut set: HashSet<char> = s1.chars().collect();
                set.retain(|c| s2.contains(c.to_string().as_str()));
                *set.iter().next().unwrap()
            })
            .map(Self::position)
            .sum()
    }

    fn part2(&self) -> usize {
        self.input
            .chunks(3)
            .map(|s| {
                let mut s1: HashSet<char> = s[0].chars().collect();
                s1.retain(|c| {
                    s[1].contains(c.to_string().as_str()) && s[2].contains(c.to_string().as_str())
                });
                *s1.iter().next().unwrap()
            })
            .map(Self::position)
            .sum()
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_sample() {
        assert_eq!(sample_day().part1(), 157)
    }

    #[test]
    fn part2_sample() {
        assert_eq!(sample_day().part2(), 70)
    }

    fn sample_day() -> Day3 {
        Day3 {
            input: vec![
                "vJrwpWtwJgWrhcsFMMfFFhFp",
                "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL",
                "PmmdzqPrVvPwwTWBwg",
                "wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn",
                "ttgJtRGJQctTZtZT",
                "CrZsJsPPZsGzwwsLwLmpwMDw",
            ]
            .iter()
            .map(|s| s.to_string())
            .collect(),
        }
    }
}
