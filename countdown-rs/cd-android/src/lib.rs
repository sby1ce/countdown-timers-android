uniffi::setup_scaffolding!();

#[uniffi::export]
pub fn update_timers(now: i64, origins: Vec<i64>) -> Vec<Vec<String>> {
    cd_core::update_timers_(now, origins)
        .into_iter()
        .map(Vec::from)
        .collect()
}
