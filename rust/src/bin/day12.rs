extern crate core;

use std::cmp::Ordering;
use std::collections::BinaryHeap;

use aoc_2022::shared::{read_input_lines, Day};

fn main() {
    Day12::from(read_input_lines(12)).run()
}

struct Day12 {
    input: Vec<Vec<Node>>,
}

impl Day<usize> for Day12 {
    fn part1(&self) -> usize {
        self.solve(vec![State::new(self.find_node('S'), 0)])
    }

    fn part2(&self) -> usize {
        let mut initial = self.find_nodes('a');
        initial.push(self.find_node('S'));
        self.solve(initial.iter().map(|n| State::new(n, 0)).collect())
    }
}

impl<S: AsRef<str>> From<Vec<S>> for Day12 {
    fn from(lines: Vec<S>) -> Self {
        let nodes = lines
            .iter()
            .enumerate()
            .map(|(y, l)| {
                l.as_ref()
                    .chars()
                    .enumerate()
                    .map(move |(x, c)| Node { x, y, c })
                    .collect()
            })
            .collect();
        Self { input: nodes }
    }
}

impl Day12 {
    fn solve(&self, initial: Vec<State>) -> usize {
        let mut queue = BinaryHeap::from(initial);
        let mut visited = Vec::default();
        let end = self.find_node('E');
        while !queue.is_empty() {
            let state = queue.pop().unwrap();
            if visited.contains(&state.node) {
                continue;
            }
            if end == state.node {
                return state.cost;
            }
            visited.push(state.node);
            self.adjacent(state.node)
                .iter()
                .filter(|n| state.node.distance(n) < 2)
                .map(|n| State::new(n, state.cost + 1))
                .for_each(|s| queue.push(s))
        }
        panic!("End not found")
    }

    fn find_nodes(&self, value: char) -> Vec<&Node> {
        self.input
            .iter()
            .flatten()
            .filter(|n| value == n.c)
            .collect()
    }

    fn find_node(&self, value: char) -> &Node {
        self.find_nodes(value)
            .first()
            .unwrap_or_else(|| panic!("Unable to find char {}", value))
    }

    fn adjacent(&self, node: &Node) -> Vec<&Node> {
        let mut neighbours = vec![(node.x + 1, node.y), (node.x, node.y + 1)];
        if node.x > 0 {
            neighbours.push((node.x - 1, node.y))
        }
        if node.y > 0 {
            neighbours.push((node.x, node.y - 1))
        }
        neighbours
            .iter()
            .filter_map(|(x, y)| self.input.get(*y).and_then(|v| v.get(*x)))
            .collect()
    }
}

#[derive(Eq, PartialEq)]
struct Node {
    pub x: usize,
    pub y: usize,
    pub c: char,
}

impl Node {
    fn distance(&self, other: &Node) -> isize {
        other.height() - self.height()
    }

    fn height(&self) -> isize {
        let c = match self.c {
            'S' => 'a',
            'E' => 'z',
            _ => self.c,
        };
        c as isize
    }
}

#[derive(Eq, PartialEq)]
struct State<'a> {
    pub node: &'a Node,
    pub cost: usize,
}

impl<'a> State<'a> {
    fn new(node: &'a Node, cost: usize) -> Self {
        Self { node, cost }
    }
}

impl<'a> Ord for State<'a> {
    fn cmp(&self, other: &Self) -> Ordering {
        other.cost.cmp(&self.cost)
    }
}

impl<'a> PartialOrd for State<'a> {
    fn partial_cmp(&self, other: &Self) -> Option<Ordering> {
        Some(self.cmp(other))
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_sample() {
        assert_eq!(sample_day().part1(), 31)
    }

    #[test]
    fn part2_sample() {
        assert_eq!(sample_day().part2(), 29)
    }

    fn sample_day() -> Day12 {
        Day12::from(vec![
            "Sabqponm", "abcryxxl", "accszExk", "acctuvwj", "abdefghi",
        ])
    }
}
