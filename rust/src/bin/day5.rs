use itertools::Itertools;

use aoc_2022::shared::{read_input_lines_preserving_empty, Day};

fn main() {
    Day5 {
        input: read_input_lines_preserving_empty(5),
    }
    .run()
}

struct Day5 {
    input: Vec<String>,
}

impl Day<String> for Day5 {
    fn part1(&self) -> String {
        let (mut stacks, moves) = self.parse();
        moves
            .iter()
            .for_each(|(items, from, to)| self.move_crate(&mut stacks, *items, *from, *to, false));
        stacks.iter().map(|l| l.last().unwrap()).join("")
    }

    fn part2(&self) -> String {
        let (mut stacks, moves) = self.parse();
        moves
            .iter()
            .for_each(|(items, from, to)| self.move_crate(&mut stacks, *items, *from, *to, true));
        stacks.iter().map(|l| l.last().unwrap()).join("")
    }
}

impl Day5 {
    fn move_crate(
        &self,
        stacks: &mut Vec<Vec<char>>,
        items: usize,
        from: usize,
        to: usize,
        preserve_order: bool,
    ) {
        let crates: Vec<_> = (0..items)
            .map(|_| stacks.get_mut(from).unwrap().pop().unwrap())
            .collect();
        let crates: Vec<_> = if preserve_order {
            crates.iter().rev().collect()
        } else {
            crates.iter().collect()
        };
        stacks.get_mut(to).unwrap().extend(crates)
    }

    fn parse(&self) -> (Vec<Vec<char>>, Vec<(usize, usize, usize)>) {
        let (stacks, moves) = self.input.split(|s| s.is_empty()).collect_tuple().unwrap();
        (Self::stacks(stacks), Self::moves(moves))
    }

    fn stacks(input: &[String]) -> Vec<Vec<char>> {
        let indexes: Vec<usize> = input
            .last()
            .unwrap()
            .chars()
            .enumerate()
            .filter(|(_, c)| c.is_digit(10))
            .map(|(i, _)| i)
            .collect();
        indexes
            .iter()
            .map(|i| {
                input
                    .iter()
                    .filter_map(|l| l.chars().nth(*i))
                    .filter(|c| c.is_alphabetic())
                    .rev()
                    .collect()
            })
            .collect()
    }

    fn moves(input: &[String]) -> Vec<(usize, usize, usize)> {
        input
            .iter()
            .map(|l| {
                l.split_ascii_whitespace()
                    .map(|s| s.trim())
                    .filter_map(|s| s.parse::<usize>().ok())
                    .collect::<Vec<_>>()
            })
            .map(|c| (c[0], c[1] - 1, c[2] - 1))
            .collect()
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_sample() {
        assert_eq!(sample_day().part1(), "CMZ")
    }

    #[test]
    fn part2_sample() {
        assert_eq!(sample_day().part2(), "MCD")
    }

    fn sample_day() -> Day5 {
        Day5 {
            input: vec![
                "    [D]",
                "[N] [C]",
                "[Z] [M] [P]",
                " 1   2   3",
                "",
                "move 1 from 2 to 1",
                "move 3 from 1 to 3",
                "move 2 from 2 to 1",
                "move 1 from 1 to 2",
            ]
            .iter()
            .map(|s| s.to_string())
            .collect(),
        }
    }
}
