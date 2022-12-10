use std::ops::RangeInclusive;

use aoc_2022::shared::{read_input_lines, Day};

fn main() {
    Day4 {
        input: read_input_lines(4)
            .iter()
            .map(|l| l.split_once(',').unwrap())
            .map(|(p1, p2)| (map_range(p1), map_range(p2)))
            .collect(),
    }
    .run()
}

fn map_range(range: &str) -> RangeInclusive<usize> {
    let (s1, s2) = range.split_once('-').unwrap();
    s1.parse().unwrap()..=s2.parse().unwrap()
}

struct Day4 {
    input: Vec<(RangeInclusive<usize>, RangeInclusive<usize>)>,
}

impl Day<usize> for Day4 {
    fn part1(&self) -> usize {
        self.input
            .iter()
            .filter(|(r1, r2)| {
                r1.clone().all(|i| r2.contains(&i)) || r2.clone().all(|i| r1.contains(&i))
            })
            .count()
    }

    fn part2(&self) -> usize {
        self.input
            .iter()
            .filter(|(r1, r2)| r1.clone().any(|i| r2.contains(&i)))
            .count()
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_sample() {
        assert_eq!(sample_day().part1(), 2)
    }

    #[test]
    fn part2_sample() {
        assert_eq!(sample_day().part2(), 4)
    }

    fn sample_day() -> Day4 {
        Day4 {
            input: vec![
                (2..=4, 6..=8),
                (2..=3, 4..=5),
                (5..=7, 7..=9),
                (2..=8, 3..=7),
                (6..=6, 4..=6),
                (2..=6, 4..=8),
            ],
        }
    }
}
