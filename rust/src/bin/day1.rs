use aoc_2022::shared::{read_input_lines_preserving_empty, Day};

fn main() {
    Day1 {
        input: read_input_lines_preserving_empty(1),
    }
    .run()
}

struct Day1 {
    input: Vec<String>,
}

impl Day1 {
    fn elves_calories(&self) -> Vec<usize> {
        self.input
            .split(|s| s.is_empty())
            .map(|l| l.iter().map(|s| s.parse::<usize>().unwrap()).sum())
            .collect()
    }
}

impl Day<usize> for Day1 {
    fn part1(&self) -> usize {
        *self.elves_calories().iter().max().unwrap()
    }

    fn part2(&self) -> usize {
        let mut elves = self.elves_calories();
        elves.sort_by(|a, b| b.cmp(a));
        elves.iter().take(3).sum()
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_sample() {
        assert_eq!(sample_day().part1(), 24000)
    }

    #[test]
    fn part2_sample() {
        assert_eq!(sample_day().part2(), 45000)
    }

    fn sample_day() -> Day1 {
        Day1 {
            input: vec![
                "1000", "2000", "3000", "", "4000", "", "5000", "6000", "", "7000", "8000", "9000",
                "", "10000",
            ]
            .iter()
            .map(|s| s.to_string())
            .collect(),
        }
    }
}
