use aoc_2022::shared::{read_input_lines, Day};

fn main() {
    Day2 {
        input: read_input_lines(2)
            .iter()
            .map(|l| {
                let chars: Vec<char> = l
                    .split_whitespace()
                    .map(|w| w.chars().nth(0).unwrap())
                    .collect();
                (chars[0], chars[1])
            })
            .collect(),
    }
    .run()
}

struct Day2 {
    input: Vec<(char, char)>,
}

impl Day2 {
    fn score(a: usize, b: usize) -> usize {
        ((4 + b - a) % 3) * 3 + b
    }
}

impl Day for Day2 {
    fn part1(&self) -> usize {
        self.input
            .iter()
            .map(|(a, b)| {
                let a = *a as usize - 'A' as usize + 1;
                let b = *b as usize - 'X' as usize + 1;
                Self::score(a, b)
            })
            .sum()
    }

    fn part2(&self) -> usize {
        self.input
            .iter()
            .map(|(a, b)| {
                let a = *a as usize - 'A' as usize + 1;
                let b = *b as usize - 'X' as usize + 1;
                let b = 1 + (a + b) % 3;
                Self::score(a, b)
            })
            .sum()
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_sample() {
        assert_eq!(sample_day().part1(), 15)
    }

    #[test]
    fn part2_sample() {
        assert_eq!(sample_day().part2(), 12)
    }

    fn sample_day() -> Day2 {
        Day2 {
            input: vec![('A', 'Y'), ('B', 'X'), ('C', 'Z')],
        }
    }
}
