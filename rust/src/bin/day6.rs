extern crate core;

use std::collections::HashSet;

use itertools::Itertools;

use aoc_2022::shared::{read_input_lines_preserving_empty, Day};

fn main() {
    Day6 {
        input: read_input_lines_preserving_empty(6)
            .first()
            .unwrap()
            .to_string(),
    }
    .run()
}

struct Day6 {
    input: String,
}

impl Day<usize> for Day6 {
    fn part1(&self) -> usize {
        self.index_of_marker(4)
    }

    fn part2(&self) -> usize {
        self.index_of_marker(14)
    }
}

impl Day6 {
    fn index_of_marker(&self, window: usize) -> usize {
        self.input
            .as_bytes()
            .windows(window)
            .map(|a| HashSet::<u8>::from_iter(a.iter().cloned()).len())
            .enumerate()
            .find_or_first(|(_, s)| *s == window)
            .map(|(i, _)| i + window)
            .unwrap()
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_sample() {
        assert_eq!(sample_day().part1(), 11)
    }

    #[test]
    fn part2_sample() {
        assert_eq!(sample_day().part2(), 26)
    }

    fn sample_day() -> Day6 {
        Day6 {
            input: vec!["zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"]
                .iter()
                .map(|s| s.to_string())
                .collect(),
        }
    }
}
